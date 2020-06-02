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
    public void invalidCommandsTest() {
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock, null));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock, ""));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock, " "));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock, "cmd"));
    }

    @Test
    public void helpTest() {
        assertTrue("Help command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "help") instanceof Help);
        assertTrue("Help command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "HELP") instanceof Help);
        assertTrue("Help command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "help please") instanceof Help);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock, "hlp"));
    }

    @Test
    public void registerTest() {
        assertTrue("Register command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "register name name username password") instanceof Register);
        assertTrue("Register command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "Register name name username password") instanceof Register);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "register name username password"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "register username password"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "register username"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "register"));
    }

    @Test
    public void loginTest() {
        assertTrue("Login command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "login username password") instanceof Login);
        assertTrue("Login command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "logIN username password") instanceof Login);
        assertTrue("Login command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "logIN username password name") instanceof Login);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "login username"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "login"));
    }

    @Test
    public void logoutTest() {
        assertTrue("Logout command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "logout") instanceof Logout);
        assertTrue("Logout command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "logOUT") instanceof Logout);
        assertTrue("Logout command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "logOUT username password") instanceof Logout);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "log out"));
    }

    @Test
    public void statusTest() {
        assertTrue("Status command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get-status") instanceof GetStatus);
        assertTrue("Status command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "GET-status") instanceof GetStatus);
        assertTrue("Status command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get-status please") instanceof GetStatus);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get status"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "status"));
    }

    @Test
    public void historyTest() {
        assertTrue("History command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get-history") instanceof History);
        assertTrue("History command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get-HistorY") instanceof History);
        assertTrue("History command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get-history please") instanceof History);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get history"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "history"));
    }

    @Test
    public void addGroupTest() {
        assertTrue("Add group command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "add-group name member member member member") instanceof AddGroup);
        assertTrue("Add group command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "add-group name member member") instanceof AddGroup);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "add-group member"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "add-group"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "add group"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "group"));
    }

    @Test
    public void splitTest() {
        assertTrue("Split command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split name amount reason") instanceof SplitFriend);
        assertTrue("Split command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split name amount reason reason") instanceof SplitFriend);
        assertTrue("Split command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "SPLIT name amount reason") instanceof SplitFriend);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split name amount"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split name"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split"));
    }

    @Test
    public void splitGroupTest() {
        assertTrue("Split group command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split-group name amount reason") instanceof SplitGroup);
        assertTrue("Split group command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split-group name amount reason reason") instanceof SplitGroup);
        assertTrue("Split group command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "Split-Group name amount reason") instanceof SplitGroup);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split-group name amount"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split-group name"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split-group"));
    }

    @Test
    public void payTest() {
        assertTrue("Pay command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "payed name amount") instanceof Payed);
        assertTrue("Pay command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "payed name amount reason") instanceof Payed);
        assertTrue("Pay command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "Payed name amount") instanceof Payed);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "payed name"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "payed"));
    }

    @Test
    public void remindTest() {
        assertTrue("Remind command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "remind name amount") instanceof Remind);
        assertTrue("Remind command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "remind name amount reason") instanceof Remind);
        assertTrue("Remind command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "REMIND name amount") instanceof Remind);
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "remind name"));
        assertNull("Invalid command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "remind"));
    }
}
