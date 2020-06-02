package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

public class Help implements Command {

    @Override
    public String execute() {
        return "Allowed commands:\n\n" +
                Commands.REGISTER.getName() + " <first name> <last name> <username> <password>\n" +
                Commands.LOG_IN.getName() + " <username> <password>\n" +
                Commands.LOGOUT.getName() + "\n" +
                Commands.DISCONNECT.getName() + "\n" +
                Commands.STATUS.getName() + "\n" +
                Commands.HISTORY.getName() + "\n" +
                Commands.ADD_FRIEND.getName() + " <friend's username>\n" +
                Commands.ADD_GROUP.getName() + " <group name> <member1> <member2> ...\n" +
                Commands.SPLIT.getName() + " <friend's username> <amount> <reason for payment>\n" +
                Commands.SPLIT_GROUP.getName() + " <group name> <amount> <reason for payment>\n" +
                Commands.PAY.getName() + " <friend's username> <amount>\n" +
                Commands.REMIND.getName() + " <friend's username> <amount>\n";
    }
}