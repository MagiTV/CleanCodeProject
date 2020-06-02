package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Commands;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private static final String HOST = "localhost";
    private static final int PORT = 3120;
    private static final int BUFFER_SIZE = 4096;
    private static final String directory = "resources";
    private static final String accountsFile = "accounts.txt";
    private static final String groupsFile = "groups.txt";
    private static final String logFile = "logs.txt";
    private final Repository repository = new Repository(this, directory, accountsFile, groupsFile, logFile);
    private ByteBuffer buffer;

    public Server() {
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            repository.restoreData();

            while (true) {
                int readyChannels = selector.select();
                if (readyChannels <= 0) {
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey currentKey = keyIterator.next();
                    if (currentKey.isAcceptable()) {
                        acceptKey(currentKey, selector);
                    } else if (currentKey.isReadable()) {
                        executeCommand(currentKey, readFromChannel(currentKey));
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException ex) {
            repository.reportLog(ex, "Connection problem.\n", Arrays.toString(ex.getStackTrace()));
        }
    }

    private void acceptKey(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
        SocketChannel acceptedSocketChannel = socketChannel.accept();
        acceptedSocketChannel.configureBlocking(false);
        acceptedSocketChannel.register(selector, SelectionKey.OP_READ);
    }

    private String readFromChannel(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        buffer.clear();
        int readBytes;
        try {
            readBytes = socketChannel.read(buffer);
        } catch (SocketException ex) {
            repository.reportLog(ex, "Problem with connecting to the client.\n", "key: " + key + "\n"
                    + Arrays.toString(ex.getStackTrace()));
            repository.disconnect(key);
            return null;
        }
        if (readBytes <= 0) {
            return null;
        }
        buffer.flip();
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    public void writeToChannel(SelectionKey key, String message) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        buffer.clear();
        buffer.put((message + System.lineSeparator()).getBytes());
        buffer.flip();
        socketChannel.write(buffer);
    }

    public void closeKey(SelectionKey key) throws IOException {
        key.channel().close();
    }

    public void executeCommand(SelectionKey key, String command) {
        Command currentCommand = CommandFactory.getCommand(repository, key, command);
        try {
            if (currentCommand == null) {
                if (repository.checkIfDisconnect(command)) {
                    writeToChannel(key, "You are disconnected.");
                    repository.disconnect(key);
                    return;
                }
                writeToChannel(key, "Wrong command. You can use \"" + Commands.HELP.getName() +
                        "\" to see all commands.");
                return;
            }
            writeToChannel(key, currentCommand.execute());
        } catch (IOException ex) {
            repository.reportLog(ex, "Problem with executing a command.\n",
                    "key: " + key + " command: " + command + "\n" + Arrays.toString(ex.getStackTrace()));
        }
    }
}
