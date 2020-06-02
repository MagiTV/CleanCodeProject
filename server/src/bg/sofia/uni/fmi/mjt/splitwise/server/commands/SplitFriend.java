package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

public class SplitFriend extends PaymentAbstract {

    private final String friend;
    private final String reason;

    public SplitFriend(Repository repository, Profile profile, String friend, String amount, String reason) {
        super(repository, profile, amount);
        this.friend = friend;
        this.reason = reason;
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
        if (!profile.isFriend(friend)) {
            return "The user " + friend + " is not your friend.";
        }
        double amountPerPerson = Command.roundNumber(amount / 2);
        addDebt(friendProfile, amountPerPerson, reason, null);
        repository.savePayment(profile.getUsername(), friend, amountPerPerson, reason, null);
        repository.saveProfiles(friendProfile, profile);
        String currentStatus = currentStatus();
        return PaymentAbstract.SUCCESSFUL_DEBT + currentStatus;
    }

    private String currentStatus() {
        StringBuilder result = new StringBuilder("\nCurrent status: ");
        double currentStatus = profile.getDebtByUsername(friend);
        if (currentStatus >= 0) {
            result.append("You owe ").append(currentStatus).append(" lv.");
        } else {
            result.append("Owes you ").append(-currentStatus).append(" lv.");
        }
        return result.toString();
    }
}