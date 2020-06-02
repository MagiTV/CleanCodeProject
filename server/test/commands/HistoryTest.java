package commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.History;
import org.junit.Test;
import org.mockito.Mock;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HistoryTest {

    @Mock
    private Repository repositoryMock = mock(Repository.class);

    @Test
    public void historyValidTest() {
        Profile profile = new Profile("name", "name", "username");
        Command historyCommand = new History(repositoryMock, profile);
        Path path = Path.of("resources", "paymentHistory", "testHistory.txt");
        when(repositoryMock.getHistoryPath("username")).thenReturn(path);
        assertEquals("valid get history", "This is a test.\n", historyCommand.execute());
    }

    @Test
    public void historyStillNoHistoryTest() {
        Profile profile = new Profile("name", "name", "username");
        Command historyCommand = new History(repositoryMock, profile);
        Path path = Path.of("testHistory.txt");
        when(repositoryMock.getHistoryPath("username")).thenReturn(path);
        assertEquals("still no history", "You still have no payment history.", historyCommand.execute());
    }

    @Test
    public void historyNoLoggedInTest() {
        Command historyCommand = new History(repositoryMock, null);
        assertEquals("get history, when no logged in", Command.NO_LOG_IN, historyCommand.execute());
    }
}
