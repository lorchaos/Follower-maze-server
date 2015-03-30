package ch.clops.fmaze.network;

import ch.clops.fmaze.ClientRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rlorca on 26/03/15.
 */
public class ClientConnector implements Connector {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnector.class);

    private final ClientRegistry registry;

    public ClientConnector(ClientRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Boolean newPeer(Peer peer) {

        String clientID =  peer.read().findFirst().orElseThrow(() -> new RuntimeException("Unable to get client ID"));

        this.registry.onPeerConnected(clientID, peer);

        return true;
    }

    @Override
    public void stop() {

        logger.info("Stopping");
        // close all clients
    }
}
