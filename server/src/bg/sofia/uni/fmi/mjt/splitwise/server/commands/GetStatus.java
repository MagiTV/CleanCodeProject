package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetStatus implements Command {

    private final Repository repository;
    private final Profile profile;

    public GetStatus(Repository repository, Profile profile) {
        this.repository = repository;
        this.profile = profile;
    }

    @Override
    public String execute() {
        if (profile == null) {
            return NO_LOG_IN;
        }
        return makeFriendsMessage() + makeGroupsMessage() + makeDebtsMessage();
    }

    private String makeFriendsMessage() {
        StringBuilder message = new StringBuilder("Friends:\n");
        Collection<Profile> friends = profile.getFriends();
        for (Profile currentFriend : friends) {
            message.append(currentFriend).append("\n");
        }
        return message.toString();
    }

    private String makeGroupsMessage() {
        StringBuilder message = new StringBuilder("\nGroups:\n");
        List<String> groups = profile.getGroups();
        for (String currentGroup : groups) {
            message.append(repository.getGroupByName(currentGroup));
        }
        return message.toString();
    }

    private String makeDebtsMessage() {
        StringBuilder message = new StringBuilder("\nDebts:\n");
        Map<String, Double> debts = profile.getDebts();
        for (Map.Entry<String, Double> currentDebt : debts.entrySet()) {
            message.append(currentDebt.getKey()).append(": ");
            if (currentDebt.getValue() >= 0) {
                message.append("You owe ").append(currentDebt.getValue()).append(" lv.\n");
            } else {
                message.append("Owes you ").append(-currentDebt.getValue()).append(" lv.\n");
            }
        }
        return message.toString();
    }
}