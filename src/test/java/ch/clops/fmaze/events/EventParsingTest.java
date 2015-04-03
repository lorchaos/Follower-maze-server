package ch.clops.fmaze.events;

import ch.clops.fmaze.events.BaseEvent;
import ch.clops.fmaze.events.EventParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.PriorityQueue;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by rlorca on 25/03/15.
 */
public class EventParsingTest {
    private EventParser parser;

    /*
    | Payload    | Sequence #| Type         | From User Id | To User Id |
    |------------|-----------|--------------|--------------|------------|
    |666|F|60|50 | 666       | Follow       | 60           | 50         |
    |1|U|12|9    | 1         | Unfollow     | 12           | 9          |
    |542532|B    | 542532    | Broadcast    | -            | -          |
    |43|P|32|56  | 43        | Private Msg  | 2            | 56         |
    |634|S|32    | 634       | Status Update| 32           | -          |
     */

    @Before
    public void setUp() throws Exception {
        parser = new EventParser();
    }

    @Test
    public void invalidEvent() {
        assertFalse(parser.parse("nononono").isPresent());
    }

    @Test
    public void followEvent() {

        FollowEvent event = (FollowEvent) parser.parse("666|F|60|50").get();
        assertEquals(666, event.sequence);
        assertEquals("60", event.from);
        assertEquals("50", event.to);
    }

    @Test
    public void unfollowEvent() {

        UnfollowEvent event = (UnfollowEvent) parser.parse("1|U|12|9").get();
        assertEquals(1, event.sequence);
        assertEquals("12", event.from);
        assertEquals("9", event.to);
    }

    @Test
    public void privateMessageEvent() {

        PrivateMessageEvent event = (PrivateMessageEvent) parser.parse("43|P|32|56").get();
        assertEquals(43, event.sequence);
        assertEquals("32", event.from);
        assertEquals("56", event.to);
    }

    @Test
    public void statusUpdate() {

        StatusUpdateEvent event = (StatusUpdateEvent) parser.parse("634|S|32").get();
        assertEquals(634, event.sequence);
        assertEquals("32", event.from);
    }

    @Test
    public void broadcast() {

        BroadcastEvent event = (BroadcastEvent) parser.parse("542532|B").get();
        assertEquals(542532, event.sequence);
    }


}
