package bg.sofia.uni.fmi.mjt.splitwise.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 3120;

    public static void main(String[] args) {
        Client chatClient = new Client();
        chatClient.start();
    }

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            System.out.println("Connected to the server.");

            new Thread(() -> sendMessages(writer, scanner)).start();
            receiveMessages(reader);
        } catch (IOException ex) {
            System.out.println("Unable to connect to the server. Please try again later.");
        }
    }

    private void receiveMessages(BufferedReader reader) throws IOException {
        while (true) {
            String message = reader.readLine();
            System.out.println(message);

            if (message.equals("You are disconnected.")) {
                break;
            }
        }
    }

    private void sendMessages(PrintWriter writer, Scanner scanner) {
        System.out.print("Please, write a command. (You can use \"help\" to see all commands.)\n");
        while (true) {
            String message = scanner.nextLine();
            writer.println(message);

            if (message.equalsIgnoreCase("disconnect")) {
                break;
            }
        }
    }
}
