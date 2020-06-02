package bg.sofia.uni.fmi.mjt.splitwise.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private String name;
    private List<String> members;

    public Group(String name, List<String> members) {
        this.name = name;
        this.members = new ArrayList<>(members);
    }

    public String getName() {
        return name;
    }

    public List<String> getMembers() {
        return new ArrayList<>(members);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name + " (");
        boolean first = true;
        for (String currentMember : members) {
            if (!first) {
                result.append(", ");
            } else {
                first = false;
            }
            result.append(currentMember);
        }
        result.append(")\n");
        return result.toString();
    }
}