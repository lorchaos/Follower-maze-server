package ch.clops.fmaze.network;

/**
 * Created by rlorca on 29/03/15.
 */
public interface Connector {

    Boolean newPeer(Peer peer);

    default void stop() {
        // no op
    }
}
