package ch.clops.fmaze;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.PriorityQueue;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by rlorca on 25/03/15.
 */
public class EventParsingTest {

    /*
    | Payload    | Sequence #| Type         | From User Id | To User Id |
    |------------|-----------|--------------|--------------|------------|
    |666|F|60|50 | 666       | Follow       | 60           | 50         |
    |1|U|12|9    | 1         | Unfollow     | 12           | 9          |
    |542532|B    | 542532    | Broadcast    | -            | -          |
    |43|P|32|56  | 43        | Private Msg  | 2            | 56         |
    |634|S|32    | 634       | Status Update| 32           | -          |
     */

    @Test
    public void follow() {

        /*
        String payload = "666|F|60|50";

        Optional<? extends Event> event = new EventParser().parse(payload);

        assertTrue(event.isPresent());

        event.ifPresent(ev -> {

            assertEquals(666, ev.sequence);
            //assertEquals(60, event.);
            //assertEquals(50, event.toUser);
        });
        */
    }

    @Test
    public void broadcast() {

        //new EventParser().parse("542532|B");
        //assertEquals(542532, event.sequence);
    }
}
