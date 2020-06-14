import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.*;
import org.junit.Test;
import org.mockito.Mock;

import java.nio.channels.SelectionKey;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class CommandFactoryTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);
    @Mock
    private SelectionKey selectionKeyMock = mock(SelectionKey.class);

    @Test
    public void nullCommandTest() {
        assertNull("Null command argument", CommandFactory.getCommand(repositoryMock, selectionKeyMock, null));
    }

    @Test
    public void emptyCommandTest() {
        assertNull("Empty string command argument", CommandFactory.getCommand(repositoryMock, selectionKeyMock, ""));
    }

    @Test
    public void invalidCommandTest() {
        assertNull("Unexciting command", CommandFactory.getCommand(repositoryMock, selectionKeyMock, "cmd"));
    }

    @Test
    public void helpLowerCaseTest() {
        assertTrue("Help command, written with lower case", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "help") instanceof Help);
    }

    @Test
    public void helpUpperCaseTest() {
        assertTrue("Help command, written with upper case", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "HELP") instanceof Help);
    }

    @Test
    public void validRegisterTest() {
        assertTrue("Valid register command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "register name name username password") instanceof Register);
    }

    @Test
    public void invalidRegisterTest() {
        assertNull("Register command with fewer arguments", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "register name username password"));
    }

    @Test
    public void validLoginTest() {
        assertTrue("Valid login command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "login username password") instanceof Login);
    }

    @Test
    public void invalidLoginTest() {
        assertNull("Login command with fewer arguments", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "login username"));
    }

    @Test
    public void validLogoutTest() {
        assertTrue("Valid logout command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "logout") instanceof Logout);
    }

    @Test
    public void logoutMoreArgumentsTest() {
        assertTrue("Logout command with more arguments, must ignore them",
                CommandFactory.getCommand(repositoryMock, selectionKeyMock, "logOUT username") instanceof Logout);
    }

    @Test
    public void invalidLogoutTest() {
        assertNull("Logout command written with two words", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "log out"));
    }

    @Test
    public void validStatusTest() {
        assertTrue("Valid status command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get-status") instanceof GetStatus);
    }

    @Test
    public void invalidStatusTest() {
        assertNull("Status command without dash", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get status"));
    }

    @Test
    public void validHistoryTest() {
        assertTrue("Valid history command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get-History") instanceof History);
    }

    @Test
    public void invalidHistoryTest() {
        assertNull("History command  without dash", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get history"));
    }

    @Test
    public void validAddGroupTest() {
        assertTrue("Add group command with four members", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "add-group name member member member member") instanceof AddGroup);
    }

    @Test
    public void addGroupOnlyOneMemberTest() {
        assertNull("Add group command with only one members must be invalid",
                CommandFactory.getCommand(repositoryMock, selectionKeyMock,"add-group member"));
    }

    @Test
    public void validSplitTest() {
        assertTrue("Valid split command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split name amount reason reason") instanceof SplitFriend);
    }

    @Test
    public void invalidSplitTest() {
        assertNull("Invalid split command with fewer arguments",
                CommandFactory.getCommand(repositoryMock, selectionKeyMock, "split name"));
    }

    @Test
    public void validSplitGroupTest() {
        assertTrue("Split group command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "Split-Group name amount reason") instanceof SplitGroup);
    }

    @Test
    public void splitGroupFewerArgumentsGroupTest() {
        assertNull("Invalid split group command with fewer arguments",
                CommandFactory.getCommand(repositoryMock, selectionKeyMock, "split-group name amount"));
    }

    @Test
    public void invalidSplitGroupTest() {
        assertNull("Wrong written split group command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "Split-Grp name amount reason"));
    }

    @Test
    public void validPayTest() {
        assertTrue("Valid pay command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "payed name amount") instanceof Payed);
    }

    @Test
    public void payFewerArgumentsTest() {
        assertNull("Invalid payed command without arguments",
                CommandFactory.getCommand(repositoryMock, selectionKeyMock, "payed"));
    }

    @Test
    public void validRemindTest() {
        assertTrue("Valid remind command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "REMIND name amount reason") instanceof Remind);
    }

    @Test
    public void invalidRemindTest() {
        assertNull("Invalid remind command with fewer arguments",
                CommandFactory.getCommand(repositoryMock, selectionKeyMock, "remind name"));
    }
}
