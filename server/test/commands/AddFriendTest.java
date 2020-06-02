package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.AddFriend;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddFriendTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Test
    public void addFriendValidTest() {
        Profile profile = new Profile("name", "name", "username");
        Command addFriendCommand = new AddFriend(repositoryMock, profile, "friend");
        Profile friend = new Profile("name", "name", "friend");
        when(repositoryMock.getProfileByUsername("friend")).thenReturn(friend);
        assertEquals("valid add friend", "friend is now your friend.", addFriendCommand.execute());
        assertTrue("add friend in user profile", profile.isFriend("friend"));
        assertTrue("add friend in friend's profile", friend.isFriend("username"));
    }

    @Test
    public void addFriendAddYourselfTest() {
        Profile profile = new Profile("name", "name", "username");
        Command addFriendCommand = new AddFriend(repositoryMock, profile, "username");
        when(repositoryMock.getProfileByUsername("username")).thenReturn(profile);
        assertEquals("add yourself as a friend", "You can't add yourself as a friend.", addFriendCommand.execute());
    }

    @Test
    public void addFriendNoSuchUserTest() {
        Profile profile = new Profile("name", "name", "username");
        Command addFriendCommand = new AddFriend(repositoryMock, profile, "friend");
        when(repositoryMock.getProfileByUsername("friend")).thenReturn(null);
        assertEquals("add non-existing user", "This user does not exist.", addFriendCommand.execute());
    }

    @Test
    public void addFriendNoLoggedInTest() {
        Command addFriendCommand = new AddFriend(repositoryMock, null, "friend");
        assertEquals("add friend, when no logged in", Command.NO_LOG_IN, addFriendCommand.execute());
    }
}
