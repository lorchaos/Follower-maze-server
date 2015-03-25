package ch.clops.fmaze;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by rlorca on 25/03/15.
 */
public class EventParser {

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

        String payload = "666|F|60|50";

        Event event = new Event(payload);

        assertEquals(666, event.sequence);
        assertEquals(60, event.fromUser);
        assertEquals(50, event.toUser);
    }
}
