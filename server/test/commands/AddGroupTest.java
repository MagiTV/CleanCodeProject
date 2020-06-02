package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.AddGroup;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddGroupTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Test
    public void addGroupValidTest() {
        Profile profile = new Profile("name", "name", "username");
        Profile profile1 = new Profile("User1", "User1", "username1");
        Profile profile2 = new Profile("User2", "User2", "username2");
        String[] members = {"username1", "username2"};
        Command addGroupCommand = new AddGroup(repositoryMock, profile, "group", members);
        when(repositoryMock.getProfileByUsername("username1")).thenReturn(profile1);
        when(repositoryMock.getProfileByUsername("username2")).thenReturn(profile2);
        assertEquals("valid add group", "The group group is created successfully.", addGroupCommand.execute());
    }

    @Test
    public void addGroupTooManyMembersTest() {
        Profile profile = new Profile("name", "name", "username");
        Profile profile1 = new Profile("User1", "User1", "username1");
        String[] members = {"username1", "username1"};
        Command addGroupCommand = new AddGroup(repositoryMock, profile, "group", members);
        when(repositoryMock.getProfileByUsername("username1")).thenReturn(profile1);
        assertEquals("add group with too many members", "There are too many members.", addGroupCommand.execute());
    }

    @Test
    public void addGroupNoSuchUserTest() {
        Profile profile = new Profile("name", "name", "username");
        Profile profile1 = new Profile("User1", "User1", "username1");
        String[] members = {"username1", "username2"};
        Command addGroupCommand = new AddGroup(repositoryMock, profile, "group", members);
        when(repositoryMock.getProfileByUsername("username1")).thenReturn(profile1);
        when(repositoryMock.getProfileByUsername("username2")).thenReturn(null);
        assertEquals("add group with non-existing user", "The user username2 does not exists.",
                addGroupCommand.execute());
    }

    @Test
    public void addGroupDuplicateNamesTest() {
        Profile profile = new Profile("name", "name", "username");
        Profile profile1 = new Profile("User1", "User1", "username1");
        Profile profile2 = new Profile("User2", "User2", "username2");
        String[] members = {"username1", "username2"};
        Command addGroupCommand = new AddGroup(repositoryMock, profile, "group", members);
        when(repositoryMock.getProfileByUsername("username1")).thenReturn(profile1);
        when(repositoryMock.getProfileByUsername("username2")).thenReturn(profile2);

        when(repositoryMock.existsGroup("group")).thenReturn(true);
        assertEquals("add group with existing name", "This group name already exists.", addGroupCommand.execute());
    }

    @Test
    public void addGroupNoLoggedInTest() {
        Command addGroupCommand = new AddGroup(repositoryMock, null, "group", null);
        assertEquals("add group, when no logged in", Command.NO_LOG_IN, addGroupCommand.execute());
    }
}
