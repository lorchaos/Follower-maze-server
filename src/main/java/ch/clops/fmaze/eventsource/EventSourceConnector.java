package ch.clops.fmaze.eventsource;

import ch.clops.fmaze.events.EventParser;
import ch.clops.fmaze.events.EventProcessor;
import ch.clops.fmaze.events.EventSorter;
import ch.clops.fmaze.network.Connector;
import ch.clops.fmaze.network.Peer;

public class EventSourceConnector implements Connector {

    private final EventSorter sorter;

    public EventSourceConnector(EventProcessor visitor) {
        this.sorter = new EventSorter(visitor);
    }

    @Override
    public Boolean newPeer(Peer peer) {

        peer.read().map(new EventParser()::parse).forEach(this.sorter::on);

        // only one event source will be processed
        return false;
    }
}
