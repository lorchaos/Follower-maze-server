package ch.clops.fmaze.client;

import ch.clops.fmaze.network.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PeerRegistry {

    private static final Logger logger = LoggerFactory.getLogger(PeerRegistry.class);

    private final ConcurrentHashMap<String, Peer> connectedPeers = new ConcurrentHashMap<>();

    public void onPeerConnected(String clientID, Peer peer) {

        logger.info("Peer connected {}", clientID);
        this.connectedPeers.put(clientID, peer);
    }

    public void broadcast(String raw) {

        this.connectedPeers.values().forEach(peer -> peer.write(raw));
    }

    public void write(String to, String raw) {

        Peer peer = this.connectedPeers.get(to);
        if(peer != null) {
            peer.write(raw);
        }
    }

    public void closeAll() {

        this.connectedPeers.values().forEach(Peer::close);
        this.connectedPeers.clear();
    }
}
