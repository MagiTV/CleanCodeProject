package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;

import java.io.Serializable;
import java.util.*;

public class Profile implements Serializable {

    private static final long serialVersionUID = 312L;

    private String firstName;
    private String secondName;
    private String username;
    private Map<String, Profile> friends = new HashMap<>();
    private Set<String> groups = new HashSet<>();
    // if < 0, he\she owеs you
    // if >= 0, you owе
    private Map<String, Double> debts = new HashMap<>();
    private StringBuilder messages = new StringBuilder();

    public Profile(String firstName, String secondName, String username) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Collection<Profile> getFriends() {
        return friends.values();
    }

    public List<String> getGroups() {
        return new ArrayList<>(groups);
    }

    public Map<String, Double> getDebts() {
        return new HashMap<>(debts);
    }

    public double getDebtByUsername(String username) {
        Double result = debts.get(username);
        return result == null ? 0 : result;
    }

    public String getMessages() {
        if (messages.length() == 0) {
            return null;
        }
        String result = messages.toString();
        messages.setLength(0);
        return result;
    }

    public void addFriend(Profile friend) {
        friends.put(friend.getUsername(), friend);
    }

    public void addGroup(String group) {
        groups.add(group);
    }

    public void addDebt(String name, double amount) {
        if (debts.containsKey(name)) {
            debts.put(name, Command.roundNumber(debts.get(name) + amount));
            return;
        }
        debts.put(name, amount);
    }

    public void addMessage(String message) {
        messages.append(message).append("\n");
    }

    public boolean isFriend(String username) {
        return friends.containsKey(username);
    }

    @Override
    public String toString() {
        return firstName + " " + secondName + " (" + username + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;
        Profile profile = (Profile) o;
        return Objects.equals(username, profile.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
