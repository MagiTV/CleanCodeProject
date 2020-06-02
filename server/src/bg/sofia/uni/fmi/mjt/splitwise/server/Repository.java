package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Commands;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Repository {

    private final Server server;
    private final MemorySaver memorySaver;
    private Map<String, Profile> profiles = new HashMap<>();
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, SelectionKey> activeUsers = new HashMap<>();
    private Map<SelectionKey, String> keys = new HashMap<>();
    private Map<String, Group> groups = new HashMap<>();

    public Repository(Server server, String directory, String accountsFile, String groupFile, String logFile) {
        this.server = server;
        memorySaver = new MemorySaver(directory, accountsFile, groupFile, logFile);
    }

    public boolean checkIfDisconnect(String command) {
        if (command != null) {
            String[] parameters = command.toLowerCase().split("\\s+");
            return parameters.length > 0 && parameters[0].equals(Commands.DISCONNECT.getName());
        }
        return false;
    }

    public void disconnect(SelectionKey key) throws IOException {
        Profile profile = getProfileByKey(key);
        if (profile != null) {
            removeActiveUser(profile.getUsername(), key);
            keys.remove(key);
        }
        server.closeKey(key);
    }

    public boolean existsGroup(String name) {
        return groups.containsKey(name);
    }

    public boolean checkPassword(String username, String password) {
        if (profiles.containsKey(username)) {
            return accounts.get(username).checkPassword(password);
        }
        return false;
    }

    public boolean isActiveUser(String name) {
        return activeUsers.containsKey(name);
    }

    public void addNewProfile(Account account, Profile profile) {
        profiles.put(profile.getUsername(), profile);
        accounts.put(account.getUsername(), account);
        memorySaver.saveProfile(profile);
        memorySaver.saveAccount(account);
    }

    public void addGroup(Group group) {
        groups.put(group.getName(), group);
        memorySaver.saveGroup(group);
    }

    public void addActiveUser(String username, SelectionKey key) {
        if (getProfileByUsername(username) != null) {
            activeUsers.put(username, key);
            keys.put(key, username);
        }
    }

    public void removeActiveUser(String username, SelectionKey key) {
        activeUsers.remove(username);
        keys.remove(key);
    }

    public Profile getProfileByUsername(String username) {
        if (profiles.containsKey(username)) {
            return profiles.get(username);
        }
        return null;
    }

    public Profile getProfileByKey(SelectionKey key) {
        if (keys.containsKey(key)) {
            return profiles.get(keys.get(key));
        }
        return null;
    }

    public Group getGroupByName(String name) {
        if (groups.containsKey(name)) {
            return groups.get(name);
        }
        return null;
    }

    public Path getHistoryPath(String username) {
        return memorySaver.getHistoryPath(username);
    }

    public void sendMessageToUser(String username, String message) {
        try {
            SelectionKey key = activeUsers.get(username);
            if (key != null) {
                server.writeToChannel(key, message);
            }
        } catch (IOException ex) {
            reportLog(ex, "Problem with sending message to " + username + "\n",
                    Arrays.toString(ex.getStackTrace()));
        }
    }

    public void savePayment(String user, String debtor, double amount, String reason, String group) {
        if (reason == null) {
            reason = "";
        } else {
            reason = " //" + reason;
        }
        if (group == null) {
            group = "";
        } else {
            group = "; group: " + group;
        }
        memorySaver.savePayment(user, "[" + DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(LocalDateTime.now())
                + "]\n" + debtor + " owes you " + amount + " lv." + reason + group + "\n");
        memorySaver.savePayment(debtor, "[" + DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(LocalDateTime.now())
                + "]\n" + "You owe " + user + " " + amount + " lv." + reason + group + "\n");
    }

    public void saveProfiles(Profile... profiles) {
        for (Profile currentProfile : profiles) {
            memorySaver.saveProfile(currentProfile);
        }
    }

    public void restoreData() {
        accounts = memorySaver.restoreAccounts();
        profiles = memorySaver.restoreProfiles(accounts.keySet());
        groups = memorySaver.restoreGroups();
    }

    public void reportLog(Exception exception, String message, String moreInfo) {
        System.out.println(message);
        memorySaver.saveLog(exception, message, moreInfo);
    }
}