package ch.clops.fmaze.network;

@FunctionalInterface
public interface Connector {

    /**
     * Process a new peer.
     *
     * @param peer
     * @return if should accept more peers
     */
    Boolean newPeer(Peer peer);
}
