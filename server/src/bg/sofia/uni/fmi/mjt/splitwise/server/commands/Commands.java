package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

public enum Commands {

    HELP("help"),
    REGISTER("register"),
    LOG_IN("login"),
    LOGOUT("logout"),
    DISCONNECT("disconnect"),
    STATUS("get-status"),
    HISTORY("get-history"),
    ADD_FRIEND("add-friend"),
    ADD_GROUP("add-group"),
    SPLIT("split"),
    SPLIT_GROUP("split-group"),
    PAY("payed"),
    REMIND("remind");

    private final String name;

    Commands(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
