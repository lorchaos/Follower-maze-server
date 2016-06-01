package ch.clops.fmaze.eventsource;

import ch.clops.fmaze.events.EventSorter;
import ch.clops.fmaze.events.EventParser;
import ch.clops.fmaze.events.EventProcessor;
import ch.clops.fmaze.network.Peer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Action1;

@Slf4j
public class EventSourceConnector implements Action1<Peer> {

    private final EventSorter sorter;

    public EventSourceConnector(EventProcessor visitor) {
        this.sorter = new EventSorter(visitor);
    }

    @Override
    public void call(Peer peer) {
        peer.read().map(new EventParser()::parse).forEach(this.sorter::on);
    }
}
