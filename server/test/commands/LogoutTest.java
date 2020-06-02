package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Logout;
import org.junit.Test;
import org.mockito.Mock;

import java.nio.channels.SelectionKey;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogoutTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Mock
    private SelectionKey selectionKeyMock = mock(SelectionKey.class);

    @Test
    public void logoutValidTest() {
        Profile profile = new Profile("name", "name", "username");
        SelectionKey selectionKeyMock = mock(SelectionKey.class);
        when(repositoryMock.getProfileByKey(selectionKeyMock)).thenReturn(profile);
        Command logoutCommand = new Logout(repositoryMock, selectionKeyMock);
        assertEquals("valid logout", "You are logged out.", logoutCommand.execute());
    }

    @Test
    public void logoutNoLoggedInTest() {
        Command logoutCommand = new Logout(repositoryMock, selectionKeyMock);
        assertEquals("logout, when no logged in", Command.NO_LOG_IN, logoutCommand.execute());
    }
}
