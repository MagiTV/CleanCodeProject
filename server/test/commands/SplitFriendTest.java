package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.SplitFriend;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SplitFriendTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Test
    public void splitValidTest() {
        Profile profile = new Profile("name", "name", "username");
        Command splitCommand = new SplitFriend(repositoryMock, profile, "friend", "10", "reason");
        Profile friend = new Profile("name", "name", "friend");
        profile.addFriend(friend);
        when(repositoryMock.getProfileByUsername("friend")).thenReturn(friend);
        assertEquals("valid split", "The debt is added successfully.\nCurrent status: Owes you 5.0 lv.",
                splitCommand.execute());
    }

    @Test
    public void splitNoFriendTest() {
        Profile profile = new Profile("name", "name", "username");
        Command splitCommand = new SplitFriend(repositoryMock, profile, "friend", "10", "reason");
        Profile friend = new Profile("name", "name", "friend");
        when(repositoryMock.getProfileByUsername("friend")).thenReturn(friend);
        assertEquals("split with no friend", "The user friend is not your friend.", splitCommand.execute());
    }

    @Test
    public void splitNoSuchUserTest() {
        Profile profile = new Profile("name", "name", "username");
        Command splitCommand = new SplitFriend(repositoryMock, profile, "friend", "10", "reason");
        when(repositoryMock.getProfileByUsername("friend")).thenReturn(null);
        assertEquals("split with no existing user", "The user friend does not exist.", splitCommand.execute());
    }

    @Test
    public void splitNegativeAmountTest() {
        Profile profile = new Profile("name", "name", "username");
        Command splitCommand = new SplitFriend(repositoryMock, profile, "friend", "-10", "reason");
        assertEquals("split negative amount", Command.INVALID_AMOUNT, splitCommand.execute());
    }

    @Test
    public void splitNoLoggedInTest() {
        Command splitCommand = new SplitFriend(repositoryMock, null, "friend", "10", "reason");
        assertEquals("split, when no logged in", Command.NO_LOG_IN, splitCommand.execute());
    }
}
