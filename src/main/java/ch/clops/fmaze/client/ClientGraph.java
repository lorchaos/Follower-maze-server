package ch.clops.fmaze.client;

import java.util.HashSet;
import java.util.function.Consumer;

public class ClientGraph {

    private final HashSet<String> followers = new HashSet<>();

    private String id;

    public ClientGraph(String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }

    public void addFollower(String followerID) {
        this.followers.add(followerID);
    }

    public void removeFollower(String followerID) {
        this.followers.remove(followerID);
    }

    public void forEachFollower(Consumer<String> f) {
        this.followers.forEach(f);
    }
}
