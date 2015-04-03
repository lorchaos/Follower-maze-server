package ch.clops.fmaze.events;

import ch.clops.fmaze.client.ClientRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by rlorca on 03/04/15.
 */
public class EventSorterTest {

    private EventSorter sorter;

    @Before
    public void setup() {
        this.sorter = new EventSorter(new EventProcessor(new ClientRegistry()));
    }

    @Test
    public void emptyEvent() {
        assertEquals(0, this.sorter.on(Optional.empty()));
    }

    @Test
    public void unexpectedSequence() {
        assertEquals(1, this.sorter.on(eventWithSequence(3)));
    }

    @Test
    public void rightOrder() {
        assertEquals(0, this.sorter.on(eventWithSequence(1)));
    }

    @Test
    public void outOfOrder() {
        assertEquals(1, this.sorter.on(eventWithSequence(2)));
        assertEquals(0, this.sorter.on(eventWithSequence(1)));
    }

    private Optional<BroadcastEvent> eventWithSequence(int seq) {
        return Optional.of(new BroadcastEvent("", seq));
    }
}
