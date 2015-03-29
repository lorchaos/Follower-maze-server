package ch.clops.fmaze.network;

import ch.clops.fmaze.events.EventHandler;
import ch.clops.fmaze.events.EventParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventSourceConnector implements Connector {

    private static final Logger logger = LoggerFactory.getLogger(EventSourceConnector.class);

    private final EventHandler handler;

    public EventSourceConnector(EventHandler handler) {
        this.handler = handler;
    }

    @Override
    public Boolean newPeer(Peer peer) {

        peer.read().map(new EventParser()::parse).forEach(this.handler::on);

        return false;
    }
}
