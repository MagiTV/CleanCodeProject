package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

import java.nio.channels.SelectionKey;

public class Login implements Command {

    private final Repository repository;
    private final SelectionKey key;
    private final String username;
    private final String password;

    public Login(Repository repository, SelectionKey key, String username, String password) {
        this.repository = repository;
        this.key = key;
        this.username = username;
        this.password = password;
    }

    @Override
    public String execute() {
        if (!repository.checkPassword(username, password)) {
            return "Wrong username or password!";
        }
        if (repository.getProfileByKey(key) != null) {
            return "You have already logged in.";
        }
        repository.addActiveUser(username, key);
        StringBuilder message = new StringBuilder("Successfully logged in.\n");
        Profile profile = repository.getProfileByUsername(username);
        String messages = profile.getMessages();
        if (messages == null) {
            message.append("No notifications to show.");
        } else {
            message.append(messages);
        }
        repository.saveProfiles(profile);
        return message.toString();
    }
}