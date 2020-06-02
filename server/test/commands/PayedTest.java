package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Payed;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PayedTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Test
    public void payValidTest() {
        Profile profile = new Profile("name", "name", "username");
        Command payedCommand = new Payed(repositoryMock, profile, "friend", "10");
        Profile friend = new Profile("name", "name", "friend");
        when(repositoryMock.getProfileByUsername("friend")).thenReturn(friend);
        assertEquals("valid payed", "The payment is added successfully.\nCurrent status: You owe 10.0 lv.",
                payedCommand.execute());
    }

    @Test
    public void payNoSuchUserTest() {
        Profile profile = new Profile("name", "name", "username");
        Command payedCommand = new Payed(repositoryMock, profile, "friend", "10");
        when(repositoryMock.getProfileByUsername("friend")).thenReturn(null);
        assertEquals("payed from non-existing user", "The user friend does not exist.", payedCommand.execute());
    }

    @Test
    public void payNegativeAmountTest() {
        Profile profile = new Profile("name", "name", "username");
        Command payedCommand = new Payed(repositoryMock, profile, "friend", "-10");
        assertEquals("payed negative amount", Command.INVALID_AMOUNT, payedCommand.execute());
    }

    @Test
    public void payNoLoggedInTest() {
        Command payedCommand = new Payed(repositoryMock, null, "friend", "10");
        assertEquals("payed no logged in", Command.NO_LOG_IN, payedCommand.execute());
    }
}
