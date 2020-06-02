package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

public class Payed extends PaymentAbstract {

    private final String friend;

    public Payed(Repository repository, Profile profile, String friend, String amount) {
        super(repository, profile, amount);
        this.friend = friend;
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
        profile.addDebt(friend, amount);
        friendProfile.addDebt(profile.getUsername(), -amount);
        repository.savePayment(friend, profile.getUsername(), amount, null, null);
        repository.saveProfiles(friendProfile, profile);
        String currentStatus = currentStatus(friend);
        return "The payment is added successfully." + currentStatus;
    }
}