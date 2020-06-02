package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Account;
import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

public class Register implements Command {

    private final Repository repository;
    private final String firstName;
    private final String secondName;
    private final String username;
    private final String password;

    public Register(Repository repository, String firstName, String secondName, String username, String password) {
        this.repository = repository;
        this.firstName = firstName;
        this.secondName = secondName;
        this.username = username;
        this.password = password;
    }

    @Override
    public String execute() {
        if (repository.getProfileByUsername(username) != null) {
            return "This username already exists.";
        }
        Profile profile = new Profile(firstName, secondName, username);
        Account account = new Account(username, password);
        repository.addNewProfile(account, profile);
        return "Welcome! Now you can login.";
    }
}