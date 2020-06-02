package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Remind;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RemindTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Test
    public void remindValidTest() {
        Profile profile = new Profile("name", "name", "username");
        Command remindCommand = new Remind(repositoryMock, profile, "friend", "10");
        Profile friend = new Profile("name", "name", "friend");
        when(repositoryMock.getProfileByUsername("friend")).thenReturn(friend);
        when(repositoryMock.isActiveUser("friend")).thenReturn(false);
        assertEquals("valid remind", "The reminder is send successfully.", remindCommand.execute());
        assertEquals("successful added message", "username: Payed you 10.0 lv. Please, add the payment.\n",
                friend.getMessages());
    }

    @Test
    public void remindNoSuchUserTest() {
        Profile profile = new Profile("name", "name", "username");
        Command remindCommand = new Remind(repositoryMock, profile, "friend", "10");
        when(repositoryMock.getProfileByUsername("friend")).thenReturn(null);
        when(repositoryMock.isActiveUser("friend")).thenReturn(false);
        assertEquals("remind no existing user", "The user friend does not exist.", remindCommand.execute());
    }

    @Test
    public void remindNegativeAmountTest() {
        Profile profile = new Profile("name", "name", "username");
        Command remindCommand = new Remind(repositoryMock, profile, "friend", "-10");
        assertEquals("remind negative amount", Command.INVALID_AMOUNT, remindCommand.execute());
    }

    @Test
    public void remindNoLoggedInTest() {
        Command remindCommand = new Remind(repositoryMock, null, "friend", "10");
        assertEquals("remind, when no logged in", Command.NO_LOG_IN, remindCommand.execute());
    }
}
