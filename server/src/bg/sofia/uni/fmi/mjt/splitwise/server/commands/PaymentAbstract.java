package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

public abstract class PaymentAbstract implements Command {

    protected static final String SUCCESSFUL_DEBT = "The debt is added successfully.";
    protected final Repository repository;
    protected final Profile profile;
    protected final Double amount;

    public PaymentAbstract(Repository repository, Profile profile, String amount) {
        this.repository = repository;
        this.profile = profile;
        this.amount = Command.makeStringToDouble(amount);
    }

    private void sendMessage(Profile debtor, double amount, String reason, String group) {
        if (group == null) {
            group = "";
        } else {
            group = "group: " + group + "; ";
        }
        String message = "You owe " + profile.getUsername() + " " + amount + " lv. //" + group + reason;
        if (repository.isActiveUser(debtor.getUsername())) {
            repository.sendMessageToUser(debtor.getUsername(), message);
        } else {
            debtor.addMessage(message);
        }
    }

    protected void addDebt(Profile debtor, double amount, String reason, String group) {
        profile.addDebt(debtor.getUsername(), -amount);
        debtor.addDebt(profile.getUsername(), amount);
        sendMessage(debtor, amount, reason, group);
    }

    protected String currentStatus(String friend) {
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
