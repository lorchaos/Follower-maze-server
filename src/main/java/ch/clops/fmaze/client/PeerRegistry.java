package ch.clops.fmaze.client;

import ch.clops.fmaze.network.Peer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PeerRegistry {

    private final ConcurrentHashMap<String, Peer> connectedPeers = new ConcurrentHashMap<>();

    public void onPeerConnected(String clientID, Peer peer) {

        log.info("Peer connected {}", clientID);
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
