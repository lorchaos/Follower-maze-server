package ch.clops.fmaze.network;

import ch.clops.fmaze.Client;
import ch.clops.fmaze.events.EventOrder;
import ch.clops.fmaze.events.EventVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rlorca on 26/03/15.
 */
public class ClientConnector implements Connector {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnector.class);

    private final EventVisitor handler;

    public ClientConnector(EventVisitor handler) {
        this.handler = handler;
    }

    @Override
    public Boolean newPeer(Peer peer) {

        String clientID =  peer.read().findFirst().orElseThrow(() -> new RuntimeException("Unable to get client ID"));

        Client client = new Client(clientID, peer);

        this.handler.on(client);

        return true;
    }
}
