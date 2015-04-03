package ch.clops.fmaze.network;

public interface Connector {

    Boolean newPeer(Peer peer);

    default void stop() {
        // no op
    }
}
