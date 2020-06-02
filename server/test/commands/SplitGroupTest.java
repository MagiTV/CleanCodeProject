package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.SplitGroup;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SplitGroupTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Test
    public void splitGroupValidTest() {
        Profile profile = new Profile("name", "name", "username");
        Command splitGroupCommand = new SplitGroup(repositoryMock, profile, "group", "10", "reason");
        Profile profile1 = new Profile("User1", "User1", "username1");
        Profile profile2 = new Profile("User2", "User2", "username2");
        Profile profile3 = new Profile("User3", "User3", "username3");
        List<String> members = new ArrayList<>();
        members.add("username1");
        members.add("username2");
        members.add("username3");
        members.add("username");
        when(repositoryMock.getProfileByUsername("username")).thenReturn(profile);
        when(repositoryMock.getProfileByUsername("username1")).thenReturn(profile1);
        when(repositoryMock.getProfileByUsername("username2")).thenReturn(profile2);
        when(repositoryMock.getProfileByUsername("username3")).thenReturn(profile3);
        Group group = new Group("group", members);
        when(repositoryMock.getGroupByName("group")).thenReturn(group);
        when(repositoryMock.isActiveUser("username2")).thenReturn(true);
        assertEquals("valid split group", "The debt is added successfully.", splitGroupCommand.execute());
    }

    @Test
    public void splitGroupUserNotMemberTest() {
        Profile profile = new Profile("name", "name", "username");
        Command splitGroupCommand = new SplitGroup(repositoryMock, profile, "group", "10", "reason");
        Profile profile1 = new Profile("User1", "User1", "username1");
        Profile profile2 = new Profile("User2", "User2", "username2");
        Profile profile3 = new Profile("User3", "User3", "username3");
        List<String> members = new ArrayList<>();
        members.add("username1");
        members.add("username2");
        members.add("username3");
        Group group = new Group("group", members);
        when(repositoryMock.getGroupByName("group")).thenReturn(group);
        when(repositoryMock.getProfileByUsername("username")).thenReturn(profile);
        when(repositoryMock.getProfileByUsername("username1")).thenReturn(profile1);
        when(repositoryMock.getProfileByUsername("username2")).thenReturn(profile2);
        when(repositoryMock.getProfileByUsername("username3")).thenReturn(profile3);
        assertEquals("split group, when no member", "You are not member of this group.", splitGroupCommand.execute());
    }

    @Test
    public void splitGroupNoSuchGroupTest() {
        Profile profile = new Profile("name", "name", "username");
        Command splitGroupCommand = new SplitGroup(repositoryMock, profile, "group", "10", "reason");
        when(repositoryMock.getGroupByName("group")).thenReturn(null);
        assertEquals("split non existing group", "The group group does not exists.", splitGroupCommand.execute());
    }

    @Test
    public void splitGroupNegativeAmountTest() {
        Profile profile = new Profile("name", "name", "username");
        Command splitGroupCommand = new SplitGroup(repositoryMock, profile, "group", "-10", "reason");
        assertEquals("split group negative amount", Command.INVALID_AMOUNT, splitGroupCommand.execute());
    }

    @Test
    public void splitGroupNoLoggedInTest() {
        Command splitGroupCommand = new SplitGroup(repositoryMock, null, "group", "10", "reason");
        assertEquals("split group, when no logged in", Command.NO_LOG_IN, splitGroupCommand.execute());
    }
}
