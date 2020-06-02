import bg.sofia.uni.fmi.mjt.splitwise.server.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class RepositoryTest {

    private Server server = mock(Server.class);
    private Repository repository =
            new Repository(server, "resources", "accountsTest.txt", "groupsTest.txt", "logsTest.txt");

    @Test
    public void checkIfDisconnectTest() {
        assertTrue("Valid disconnect", repository.checkIfDisconnect("disconnect"));
        assertTrue("Valid disconnect", repository.checkIfDisconnect("disconnect me"));
        assertFalse("Invalid disconnect", repository.checkIfDisconnect("discon"));
        assertFalse("Invalid disconnect", repository.checkIfDisconnect(null));
    }

    @Test
    public void disconnectTest() throws IOException {
        Account account = new Account("username", "password");
        Profile profile = new Profile("Profile", "Profile", "username");
        repository.addNewProfile(account, profile);
        SelectionKey selectionKeyMock = mock(SelectionKey.class);
        repository.addActiveUser("username", selectionKeyMock);
        repository.disconnect(selectionKeyMock);

        assertFalse(repository.isActiveUser("username"));
    }

    @Test
    public void addUserTest() {
        Account account = new Account("username", "password");
        Profile profile = new Profile("Profile", "Profile", "username");
        repository.addNewProfile(account, profile);

        assertNotNull("Existing profile", repository.getProfileByUsername("username"));
        assertNull("Non-existing profile", repository.getProfileByUsername("profile"));
    }

    @Test
    public void getUserByNameTest() {
        Account account = new Account("username", "password");
        Profile profile = new Profile("Profile", "Profile", "username");
        repository.addNewProfile(account, profile);

        assertEquals("Existing profile", profile, repository.getProfileByUsername("username"));
        assertNull("Non-existing profile", repository.getProfileByUsername("profile"));
    }

    @Test
    public void checkPasswordTest() {
        Account account = new Account("username", "password");
        Profile profile = new Profile("Profile", "Profile", "username");
        repository.addNewProfile(account, profile);

        assertTrue("Valid password", repository.checkPassword("username", "password"));
        assertFalse("Non-existing profile", repository.checkPassword("profile", "password"));
        assertFalse("Invalid password", repository.checkPassword("username", "pasword"));
    }

    @Test
    public void addActiveUserTest() {
        Account account = new Account("username", "password");
        Profile profile = new Profile("Profile", "Profile", "username");
        repository.addNewProfile(account, profile);
        SelectionKey selectionKeyMock = mock(SelectionKey.class);
        repository.addActiveUser("username", selectionKeyMock);

        assertTrue("Active profile", repository.isActiveUser("username"));
        assertFalse("Inactive profile", repository.isActiveUser("profile"));
    }

    @Test
    public void removeActiveUserTest() {
        Account account = new Account("username", "password");
        Profile profile = new Profile("Profile", "Profile", "username");
        repository.addNewProfile(account, profile);
        SelectionKey selectionKeyMock = mock(SelectionKey.class);
        repository.addActiveUser("username", selectionKeyMock);
        repository.removeActiveUser("username", selectionKeyMock);

        assertFalse("Inactive profile", repository.isActiveUser("username"));
    }

    @Test
    public void getUserByKeyTest() {
        Account account = new Account("username", "password");
        Profile profile = new Profile("Profile", "Profile", "username");
        repository.addNewProfile(account, profile);
        SelectionKey selectionKeyMock = mock(SelectionKey.class);
        SelectionKey selectionKeyMock2 = mock(SelectionKey.class);
        repository.addActiveUser("username", selectionKeyMock);

        assertEquals("Existing profile", profile, repository.getProfileByKey(selectionKeyMock));
        assertNull("Non-existing profile", repository.getProfileByKey(selectionKeyMock2));
    }

    @Test
    public void addGroupTest() {
        List<String> members = new ArrayList<>();
        members.add("username1");
        members.add("username2");
        members.add("username3");
        Group group = new Group("group", members);
        repository.addGroup(group);

        assertTrue("Existing group", repository.existsGroup("group"));
        assertFalse("Non-existing group", repository.existsGroup("grp"));
    }
}
