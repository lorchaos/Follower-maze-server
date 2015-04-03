package ch.clops.fmaze.client;

import ch.clops.fmaze.network.Connector;
import ch.clops.fmaze.network.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PeerConnector implements Connector {

    private static final Logger logger = LoggerFactory.getLogger(PeerConnector.class);

    private final PeerRegistry registry;

    public PeerConnector(PeerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Boolean newPeer(Peer peer) {

        Optional<String> id = peer.read().findFirst();
        if(id.isPresent()) {
            this.registry.onPeerConnected(id.get(), peer);
        } else {
            logger.warn("Unable to get client ID");
        }

        // receives next peer
        return true;
    }
}
