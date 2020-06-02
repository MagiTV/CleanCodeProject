package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

import java.util.ArrayList;
import java.util.List;

public class AddGroup implements Command {

    private final Repository repository;
    private final Profile profile;
    private final String groupName;
    private final String[] members;

    public AddGroup(Repository repository, Profile profile, String groupName, String[] members) {
        this.repository = repository;
        this.profile = profile;
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public String execute() {
        if (profile == null) {
            return NO_LOG_IN;
        }
        if (repository.existsGroup(groupName)) {
            return "This group name already exists.";
        }
        List<String> membersList = new ArrayList<>();
        membersList.add(profile.getUsername());
        profile.addGroup(groupName);
        repository.saveProfiles(profile);
        for (String currentMember : members) {
            Profile member = repository.getProfileByUsername(currentMember);
            if (member == null) {
                return "The user " + currentMember + " does not exists.";
            }
            if (!membersList.contains(currentMember)) {
                membersList.add(currentMember);
                member.addGroup(groupName);
                repository.saveProfiles(member);
            }
        }
        if (membersList.size() < 3) {
            return "There are too many members.";
        }
        Group group = new Group(groupName, membersList);
        repository.addGroup(group);
        return "The group " + groupName + " is created successfully.";
    }
}
