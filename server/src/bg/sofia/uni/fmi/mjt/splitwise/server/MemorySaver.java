package bg.sofia.uni.fmi.mjt.splitwise.server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class MemorySaver {
    private final String directory;
    private final Path accountsStore;
    private final Path groupsStore;
    private final Path logStore;

    public MemorySaver(String directory, String accountsFile, String groupFile, String logFile) {
        this.directory = directory;
        accountsStore = Path.of(directory, accountsFile);
        groupsStore = Path.of(directory, groupFile);
        logStore = Path.of(directory, logFile);
    }

    public Path getHistoryPath(String username) {
        return Path.of(directory, "paymentHistory", username + "History.txt");
    }

    public Path getProfilePath(String username) {
        return Path.of(directory, "paymentHistory", username + "Profile.txt");
    }

    private void writeInLogFile(String log) {
        try (OutputStream os = Files.newOutputStream(logStore, CREATE, APPEND)) {
            os.write(log.getBytes());
        } catch (IOException ex) {
            System.out.println("\n\nProblem with the logs file.\n");
        }
    }

    public void saveLog(Exception exception, String message, String moreInfo) {
        writeInLogFile("\n" + message + exception.toString() + "\n" + moreInfo + "\n");
    }

    public void savePayment(String username, String description) {
        Path path = getHistoryPath(username);
        try (OutputStream os = Files.newOutputStream(path, CREATE, APPEND)) {
            os.write(description.getBytes());
        } catch (IOException ex) {
            saveLog(ex, "Problem with writing in file \"" + username + ".txt\"\n", "");
        }
    }

    public void saveAccount(Account account) {
        try (OutputStream os = Files.newOutputStream(accountsStore, CREATE, APPEND)) {
            os.write(account.toString().getBytes());
        } catch (IOException ex) {
            saveLog(ex, "Problem with accounts file.\n", "");
        }
    }

    public void saveProfile(Profile profile) {
        Path path = getProfilePath(profile.getUsername());
        try (var oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(profile);
        } catch (IOException ex) {
            saveLog(ex, "Problem with saving the profile " + profile.getUsername() + ".\n", "");
        }
    }

    public void saveGroup(Group group) {
        try (OutputStream os = Files.newOutputStream(groupsStore, CREATE, APPEND)) {
            os.write(groupToString(group).getBytes());
        } catch (IOException ex) {
            saveLog(ex, "Problem with group file.\n", "");
        }
    }

    // in specific format, easier to read
    private String groupToString(Group group) {
        StringBuilder result = new StringBuilder(group.getName() + " ");
        for (String currentMember : group.getMembers()) {
            result.append(currentMember).append(" ");
        }
        result.append("\n");
        return result.toString();
    }

    public Map<String, Profile> restoreProfiles(Set<String> usernames) {
        Map<String, Profile> users = new HashMap<>();
        Path path;
        boolean problem = false;
        for (String currentUser : usernames) {
            path = getProfilePath(currentUser);
            try (var ois = new ObjectInputStream(Files.newInputStream(path))) {
                Profile profile = (Profile) ois.readObject();
                users.put(currentUser, profile);
            } catch (FileNotFoundException | NoSuchFileException ex) {
                saveLog(ex, "Unable to find the file with the profile " + currentUser + ".\n", "");
                problem = true;
            } catch (IOException | ClassNotFoundException ex) {
                saveLog(ex, "Problem with the file with the profile " + currentUser + ".\n", "");
                problem = true;
            }
            if (problem) {
                Profile profile = new Profile("default", "default", currentUser);
                users.put(currentUser, profile);
            }
        }
        return users;
    }

    public Map<String, Group> restoreGroups() {
        try (BufferedReader reader = new BufferedReader(Files.newBufferedReader(groupsStore))) {
            String currentLine;
            Map<String, Group> result = new HashMap<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] line = currentLine.split("\\s+");
                List<String> members = new ArrayList<>(Arrays.asList(line).subList(1, line.length));
                Group group = new Group(line[0], members);
                result.put(line[0], group);
            }
            return result;
        } catch (FileNotFoundException | NoSuchFileException ex) {
            saveLog(ex, "Unable to find the file with the accounts.\n", "");
        } catch (IOException ex) {
            saveLog(ex, "Problem with the file with the accounts.\n", "");
        }
        return new HashMap<>();
    }

    public Map<String, Account> restoreAccounts() {
        try (BufferedReader reader = new BufferedReader(Files.newBufferedReader(accountsStore))) {
            String currentLine;
            Map<String, Account> result = new HashMap<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] line = currentLine.split("\\s+");
                Account currentAccount = new Account(line[0], line[1]);
                result.put(line[0], currentAccount);
            }
            return result;
        } catch (FileNotFoundException | NoSuchFileException ex) {
            saveLog(ex, "Unable to find the file with the accounts.\n", "");
        } catch (IOException ex) {
            saveLog(ex, "Problem with the file with the accounts.\n", "");
        }
        return new HashMap<>();
    }
}