package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.GetStatus;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetStatusTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Test
    public void statusValidTest() {
        Profile profile = new Profile("name", "name", "username");
        Profile profile1 = new Profile("User1", "User1", "username1");
        Profile profile2 = new Profile("User2", "User2", "username2");
        profile.addFriend(profile1);
        profile.addFriend(profile2);
        profile.addGroup("group");
        profile.addDebt(profile2.getUsername(), 10);
        profile.addDebt(profile2.getUsername(), 6);
        profile.addDebt(profile2.getUsername(), -8);
        profile.addDebt(profile1.getUsername(), -4);
        List<String> members = new ArrayList<>();
        members.add("username1");
        members.add("username2");
        members.add("username3");
        Group group = new Group("group", members);
        when(repositoryMock.getGroupByName("group")).thenReturn(group);
        Command statusCommand = new GetStatus(repositoryMock, profile);
        String result = "Friends:\n" + profile2 + "\n" + profile1 +
                "\n\nGroups:\ngroup (username1, username2, username3)\n" +
                "\nDebts:\nusername1: Owes you 4.0 lv.\nusername2: You owe 8.0 lv.\n";
        assertEquals("valid get status", result, statusCommand.execute());
    }

    @Test
    public void statusNoLoggedInTest() {
        Command statusCommand = new GetStatus(repositoryMock, null);
        assertEquals("get status, when no logged in", Command.NO_LOG_IN, statusCommand.execute());
    }
}
