package ch.clops.fmaze.client;

import ch.clops.fmaze.network.Connector;
import ch.clops.fmaze.network.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeerConnector implements Connector {

    private static final Logger logger = LoggerFactory.getLogger(PeerConnector.class);

    private final PeerRegistry registry;

    public PeerConnector(PeerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Boolean newPeer(Peer peer) {

        String clientID =  peer.read().findFirst().orElseThrow(() -> new RuntimeException("Unable to get client ID"));

        this.registry.onPeerConnected(clientID, peer);

        // receives next peer
        return true;
    }
}
