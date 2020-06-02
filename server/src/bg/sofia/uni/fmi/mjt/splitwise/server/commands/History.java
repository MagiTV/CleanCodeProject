package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class History implements Command {

    private final Repository repository;
    private final Profile profile;

    public History(Repository repository, Profile profile) {
        this.repository = repository;
        this.profile = profile;
    }

    @Override
    public String execute() {
        if (profile == null) {
            return NO_LOG_IN;
        }
        Path path = repository.getHistoryPath(profile.getUsername());
        try (BufferedReader reader =
                     new BufferedReader(Files.newBufferedReader(path))) {
            String currentLine;
            StringBuilder message = new StringBuilder();
            while ((currentLine = reader.readLine()) != null) {
                message.append(currentLine).append("\n");
            }
            return message.toString();
        } catch (FileNotFoundException | NoSuchFileException ex) {
            return "You still have no payment history.";
        } catch (IOException ex) {
            repository.reportLog(ex, "Problem with the history file of user " + profile.getUsername() + ".\n", "");
            return "Problem with finding your payment history. Please try again later.";
        }
    }
}
