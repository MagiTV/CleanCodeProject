package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

import java.nio.channels.SelectionKey;

public class Logout implements Command {

    private final Repository repository;
    private final SelectionKey key;
    private final Profile profile;

    public Logout(Repository repository, SelectionKey key) {
        this.repository = repository;
        this.key = key;
        this.profile = repository.getProfileByKey(key);
    }

    @Override
    public String execute() {
        if (profile == null) {
            return NO_LOG_IN;
        }
        repository.removeActiveUser(profile.getUsername(), key);
        return "You are logged out.";
    }
}