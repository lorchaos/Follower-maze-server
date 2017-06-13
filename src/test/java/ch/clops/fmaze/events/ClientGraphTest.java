package ch.clops.fmaze.events;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ClientGraphTest {

    public static final String ALICE = "alice";
    public static final String BOB = "bob";
    public static final String JOHN = "john";

    private ClientGraph graph;

    @Before
    public void setUp() {
        this.graph = new ClientGraph();
    }

    @Test
    public void followerIsAdded() {

        this.graph.addFollower(ALICE, BOB);

        assertEquals(Arrays.asList(BOB), getFollowerList(ALICE));
    }

    @Test
    public void followerIsRemoved() {

        this.graph.addFollower(ALICE, BOB);
        this.graph.addFollower(ALICE, JOHN);

        this.graph.removeFollower(ALICE, BOB);

        assertEquals(Arrays.asList(JOHN), getFollowerList(ALICE));
    }

    @Test
    public void usersAreIndependent() {

        this.graph.addFollower(ALICE, BOB);
        this.graph.addFollower(BOB, JOHN);

        assertEquals(Arrays.asList(BOB), getFollowerList(ALICE));
    }

    private ArrayList<String> getFollowerList(String user) {

        ArrayList<String> followers = new ArrayList<>();
        this.graph.forEachFollower(user, id -> followers.add(id));
        return followers;
    }
}
