package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

public interface Command {

    String NO_LOG_IN = "You are not logged in.";
    String INVALID_AMOUNT = "Invalid amount.";

    static Double makeStringToDouble(String number) {
        try {
            return roundNumber(Double.parseDouble(number));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    static Double roundNumber(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    String execute();
}