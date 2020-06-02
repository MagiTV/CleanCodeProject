package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Profile;
import bg.sofia.uni.fmi.mjt.splitwise.server.Repository;

import java.nio.channels.SelectionKey;
import java.util.Arrays;

public class CommandFactory {

    private final static int REGISTER_ARGUMENTS = 5;
    private final static int SIGN_IN_ARGUMENTS = 3;
    private final static int ADD_FRIEND_ARGUMENTS = 2;
    private final static int ADD_GROUP_ARGUMENTS = 4;
    private final static int SPLIT_ARGUMENTS = 4;
    private final static int SPLIT_GROUP_ARGUMENTS = 4;
    private final static int PAY_ARGUMENTS = 3;
    private final static int REMIND_ARGUMENTS = 3;

    public static Command getCommand(Repository repository, SelectionKey key, String command) {
        if (command == null) {
            return null;
        }
        String[] arguments = command.split("\\s+");
        if (arguments.length == 0) {
            return null;
        }
        Profile profile = repository.getProfileByKey(key);
        String commandName = arguments[0].toLowerCase();
        if (commandName.equals(Commands.HELP.getName())) {
            return new Help();
        }
        if (commandName.equals(Commands.REGISTER.getName())) {
            if (arguments.length < REGISTER_ARGUMENTS) {
                return null;
            }
            return new Register(repository, arguments[1], arguments[2], arguments[3], arguments[4]);
        }
        if (commandName.equals(Commands.LOG_IN.getName())) {
            if (arguments.length < SIGN_IN_ARGUMENTS) {
                return null;
            }
            return new Login(repository, key, arguments[1], arguments[2]);
        }
        if (commandName.equals(Commands.LOGOUT.getName())) {
            return new Logout(repository, key);
        }
        if (commandName.equals(Commands.STATUS.getName())) {
            return new GetStatus(repository, profile);
        }
        if (commandName.equals(Commands.HISTORY.getName())) {
            return new History(repository, profile);
        }
        if (commandName.equals(Commands.ADD_FRIEND.getName())) {
            if (arguments.length < ADD_FRIEND_ARGUMENTS) {
                return null;
            }
            return new AddFriend(repository, profile, arguments[1]);
        }
        if (commandName.equals(Commands.ADD_GROUP.getName())) {
            if (arguments.length < ADD_GROUP_ARGUMENTS) {
                return null;
            }
            return new AddGroup(repository, profile, arguments[1],
                    Arrays.copyOfRange(arguments, 2, arguments.length));
        }
        if (commandName.equals(Commands.SPLIT.getName())) {
            if (arguments.length < SPLIT_ARGUMENTS) {
                return null;
            }
            return new SplitFriend(repository, profile, arguments[1], arguments[2],
                    String.join(" ", Arrays.copyOfRange(arguments, SPLIT_ARGUMENTS - 1, arguments.length)));
        }
        if (commandName.equals(Commands.SPLIT_GROUP.getName())) {
            if (arguments.length < SPLIT_GROUP_ARGUMENTS) {
                return null;
            }
            return new SplitGroup(repository, profile, arguments[1], arguments[2],
                    String.join(" ", Arrays.copyOfRange(arguments, SPLIT_GROUP_ARGUMENTS - 1, arguments.length)));
        }
        if (commandName.equals(Commands.PAY.getName())) {
            if (arguments.length < PAY_ARGUMENTS) {
                return null;
            }
            return new Payed(repository, profile, arguments[1], arguments[2]);
        }
        if (commandName.equals(Commands.REMIND.getName())) {
            if (arguments.length < REMIND_ARGUMENTS) {
                return null;
            }
            return new Remind(repository, profile, arguments[1], arguments[2]);
        }
        return null;
    }
}