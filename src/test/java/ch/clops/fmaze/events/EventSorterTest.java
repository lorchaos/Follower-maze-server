package ch.clops.fmaze.events;

import ch.clops.fmaze.client.PeerRegistry;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class EventSorterTest {

    private EventSorter sorter;

    @Before
    public void setup() {
        this.sorter = new EventSorter();
    }

    // TODO must find a way to test this

    @Test
    public void emptyEvent() {
        this.sorter.on(Optional.empty());
    }

    @Test
    public void unexpectedSequence() {
        //assertTrue(this.sorter.on(eventWithSequence(3)).first().toList().isEmpty());
    }

    @Test
    public void rightOrder() {

        this.sorter.on(eventWithSequence(1)).first().doOnNext(e -> assertEquals(1, e.sequence)).subscribe();
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
