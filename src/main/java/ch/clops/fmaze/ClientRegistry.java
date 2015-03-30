package ch.clops.fmaze;

import ch.clops.fmaze.network.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by rlorca on 30/03/15.
 */
public class ClientRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ClientRegistry.class);

    private final HashMap<String, Peer> connectedPeers = new HashMap<>();

    public void onPeerConnected(String clientID, Peer peer) {

        logger.info("Peer connected {}", clientID);

        this.connectedPeers.put(clientID, peer);
    }

    public void write(String raw) {

        this.connectedPeers.values().forEach(v -> write(raw, v));
    }

    public void write(String to, String raw) {

        Peer peer = this.connectedPeers.get(to);
        write(raw, peer);
    }

    private void write(String raw, Peer peer) {

        if(peer != null) {
            try {
                peer.write(raw + "\r\n");
            } catch (IOException e) {
                logger.warn("Unable to write to peer", e);
            }
        }
    }
}
