import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.*;
import exceptions.InvalidCommandException;
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

    @Test (expected = InvalidCommandException.class)
    public void nullCommandTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, null);
    }

    @Test (expected = InvalidCommandException.class)
    public void emptyCommandTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "");
    }

    @Test (expected = InvalidCommandException.class)
    public void invalidCommandTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "cmd");
    }

    @Test
    public void helpLowerCaseTest() throws InvalidCommandException {
        assertTrue("Help command, written with lower case", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "help") instanceof Help);
    }

    @Test
    public void helpUpperCaseTest() throws InvalidCommandException {
        assertTrue("Help command, written with upper case", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "HELP") instanceof Help);
    }

    @Test
    public void validRegisterTest() throws InvalidCommandException {
        assertTrue("Valid register command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "register name name username password") instanceof Register);
    }

    @Test (expected = InvalidCommandException.class)
    public void invalidRegisterTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "register name username password");
    }

    @Test
    public void validLoginTest() throws InvalidCommandException {
        assertTrue("Valid login command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "login username password") instanceof Login);
    }

    @Test (expected = InvalidCommandException.class)
    public void invalidLoginTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "login username");
    }

    @Test
    public void validLogoutTest() throws InvalidCommandException {
        assertTrue("Valid logout command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "logout") instanceof Logout);
    }

    @Test
    public void logoutMoreArgumentsTest() throws InvalidCommandException {
        assertTrue("Logout command with more arguments, must ignore them",
                CommandFactory.getCommand(repositoryMock, selectionKeyMock, "logOUT username") instanceof Logout);
    }

    @Test (expected = InvalidCommandException.class)
    public void invalidLogoutTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "log out");
    }

    @Test
    public void validStatusTest() throws InvalidCommandException {
        assertTrue("Valid status command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get-status") instanceof GetStatus);
    }

    @Test (expected = InvalidCommandException.class)
    public void invalidStatusTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "get status");
    }

    @Test
    public void validHistoryTest() throws InvalidCommandException {
        assertTrue("Valid history command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "get-History") instanceof History);
    }

    @Test (expected = InvalidCommandException.class)
    public void invalidHistoryTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "get history");
    }

    @Test
    public void validAddfriendTest() throws InvalidCommandException {
        assertTrue("Valid add friend command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "add-friend friend") instanceof AddFriend);
    }

    @Test (expected = InvalidCommandException.class)
    public void addFriendWithoutArgumentTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock,"add-friend");
    }

    @Test
    public void validAddGroupTest() throws InvalidCommandException {
        assertTrue("Add group command with four members", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "add-group name member member member member") instanceof AddGroup);
    }

    @Test (expected = InvalidCommandException.class)
    public void addGroupOnlyOneMemberTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock,"add-group member");
    }

    @Test
    public void validSplitTest() throws InvalidCommandException {
        assertTrue("Valid split command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "split name amount reason reason") instanceof SplitFriend);
    }

    @Test (expected = InvalidCommandException.class)
    public void invalidSplitTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "split name");
    }

    @Test
    public void validSplitGroupTest() throws InvalidCommandException {
        assertTrue("Split group command.", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "Split-Group name amount reason") instanceof SplitGroup);
    }

    @Test (expected = InvalidCommandException.class)
    public void splitGroupFewerArgumentsGroupTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "split-group name amount");
    }

    @Test (expected = InvalidCommandException.class)
    public void invalidSplitGroupTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "Split-Grp name amount reason");
    }

    @Test
    public void validPayTest() throws InvalidCommandException {
        assertTrue("Valid pay command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "payed name amount") instanceof Payed);
    }

    @Test (expected = InvalidCommandException.class)
    public void payFewerArgumentsTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "payed");
    }

    @Test
    public void validRemindTest() throws InvalidCommandException {
        assertTrue("Valid remind command", CommandFactory.getCommand(repositoryMock, selectionKeyMock,
                "REMIND name amount reason") instanceof Remind);
    }

    @Test (expected = InvalidCommandException.class)
    public void invalidRemindTest() throws InvalidCommandException {
        CommandFactory.getCommand(repositoryMock, selectionKeyMock, "remind name");
    }
}
