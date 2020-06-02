package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Login;
import org.junit.Test;
import org.mockito.Mock;

import java.nio.channels.SelectionKey;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Mock
    private SelectionKey selectionKeyMock = mock(SelectionKey.class);

    @Test
    public void loginValidNoMessagesTest() {
        Command loginCommand = new Login(repositoryMock, selectionKeyMock, "username", "password");
        when(repositoryMock.checkPassword("username", "password")).thenReturn(true);
        when(repositoryMock.getProfileByKey(selectionKeyMock)).thenReturn(null);
        Profile profile = new Profile("name", "name", "username");
        when(repositoryMock.getProfileByUsername("username")).thenReturn(profile);
        assertEquals("valid login with no messages", "Successfully logged in.\nNo notifications to show.",
                loginCommand.execute());
    }

    @Test
    public void loginValidWithMessagesTest() {
        Command loginCommand = new Login(repositoryMock, selectionKeyMock, "username", "password");
        when(repositoryMock.checkPassword("username", "password")).thenReturn(true);
        when(repositoryMock.getProfileByKey(selectionKeyMock)).thenReturn(null);
        Profile profile = new Profile("name", "name", "username");
        profile.addMessage("message");
        when(repositoryMock.getProfileByUsername("username")).thenReturn(profile);
        assertEquals("valid login with messages", "Successfully logged in.\nmessage\n", loginCommand.execute());
    }

    @Test
    public void loginAlreadyLoggedInTest() {
        Command loginCommand = new Login(repositoryMock, selectionKeyMock, "username", "password");
        when(repositoryMock.checkPassword("username", "password")).thenReturn(true);
        Profile profile = new Profile("name", "name", "username");
        when(repositoryMock.getProfileByKey(selectionKeyMock)).thenReturn(profile);
        when(repositoryMock.getProfileByUsername("username")).thenReturn(profile);
        assertEquals("login, when already logged in", "You have already logged in.", loginCommand.execute());
    }

    @Test
    public void loginWrongPasswordTest() {
        Command loginCommand = new Login(repositoryMock, selectionKeyMock, "username", "password");
        when(repositoryMock.checkPassword("username", "password")).thenReturn(false);
        assertEquals("login with wrong password or username", "Wrong username or password!", loginCommand.execute());
    }
}
