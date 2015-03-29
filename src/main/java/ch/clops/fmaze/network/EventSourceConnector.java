package ch.clops.fmaze.network;

import ch.clops.fmaze.events.EventOrder;
import ch.clops.fmaze.events.EventParser;
import ch.clops.fmaze.events.EventVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventSourceConnector implements Connector {

    private static final Logger logger = LoggerFactory.getLogger(EventSourceConnector.class);

    private final EventOrder handler;

    public EventSourceConnector(EventVisitor visitor) {
        this.handler = new EventOrder(visitor);
    }

    @Override
    public Boolean newPeer(Peer peer) {

        peer.read().map(new EventParser()::parse).forEach(this.handler::on);

        return false;
    }
}
