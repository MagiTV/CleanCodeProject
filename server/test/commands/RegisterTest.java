package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Register;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Test
    public void registerValidTest() {
        Command registerCommand = new Register(repositoryMock, "name", "name", "username", "password");
        when(repositoryMock.getProfileByUsername("username")).thenReturn(null);
        assertEquals("valid register", "Welcome! Now you can login.", registerCommand.execute());
    }

    @Test
    public void registerDuplicateUsernamesTest() {
        Command registerCommand = new Register(repositoryMock, "name", "name", "username", "password");
        Profile profile = new Profile("name", "name", "username");
        when(repositoryMock.getProfileByUsername("username")).thenReturn(profile);
        assertEquals("register still existing username", "This username already exists.", registerCommand.execute());
    }
}
