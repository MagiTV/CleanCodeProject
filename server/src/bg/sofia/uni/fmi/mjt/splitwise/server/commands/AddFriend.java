package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

public class AddFriend implements Command {

    private final Repository repository;
    private final Profile profile;
    private final String friend;

    public AddFriend(Repository repository, Profile profile, String friend) {
        this.repository = repository;
        this.profile = profile;
        this.friend = friend;
    }

    @Override
    public String execute() {
        if (profile == null) {
            return NO_LOG_IN;
        }
        Profile friendProfile = repository.getProfileByUsername(friend);
        if (friendProfile == null) {
            return "This user does not exist.";
        }
        if (friendProfile.equals(profile)) {
            return "You can't add yourself as a friend.";
        }
        profile.addFriend(friendProfile);
        friendProfile.addFriend(profile);
        repository.saveProfiles(friendProfile, profile);
        return friend + " is now your friend.";
    }
}
