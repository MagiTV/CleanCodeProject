package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

import java.util.List;

public class SplitGroup extends PaymentAbstract {

    private final String groupName;
    private final String reason;

    public SplitGroup(Repository repository, Profile profile, String groupName, String amount, String reason) {
        super(repository, profile, amount);
        this.groupName = groupName;
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
        Group group = repository.getGroupByName(groupName);
        if (group == null) {
            return "The group " + groupName + " does not exists.";
        }
        List<String> members = group.getMembers();
        if (!members.contains(profile.getUsername())) {
            return "You are not member of this group.";
        }
        double amountPerPerson = Command.roundNumber(amount / members.size());
        Profile member;
        for (String currentMember : members) {
            if (!currentMember.equals(profile.getUsername())) {
                member = repository.getProfileByUsername(currentMember);
                addDebt(member, amountPerPerson, reason, groupName);
                repository.savePayment(profile.getUsername(), currentMember, amountPerPerson, reason, groupName);
                repository.saveProfiles(member);
            }
        }
        repository.saveProfiles(profile);
        return PaymentAbstract.SUCCESSFUL_DEBT;
    }
}