package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

public class Remind implements Command {

    private final Repository repository;
    private final Profile profile;
    private final String friend;
    private final Double amount;

    public Remind(Repository repository, Profile profile, String friend, String amount) {
        this.repository = repository;
        this.profile = profile;
        this.friend = friend;
        this.amount = Command.makeStringToDouble(amount);
    }

    @Override
    public String execute() {
        if (profile == null) {
            return NO_LOG_IN;
        }
        if (amount == null || amount <= 0) {
            return INVALID_AMOUNT;
        }
        Profile friendProfile = repository.getProfileByUsername(friend);
        if (friendProfile == null) {
            return "The user " + friend + " does not exist.";
        }
        String message = profile.getUsername() + ": Payed you " + amount + " lv. Please, add the payment.";
        if (repository.isActiveUser(friend)) {
            repository.sendMessageToUser(friend, message);
        } else {
            friendProfile.addMessage(message);
        }
        repository.saveProfiles(friendProfile);
        return "The reminder is send successfully.";
    }
}
