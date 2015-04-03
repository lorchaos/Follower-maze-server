package ch.clops.fmaze;

import ch.clops.fmaze.client.PeerRegistry;
import ch.clops.fmaze.events.EventProcessor;
import ch.clops.fmaze.client.PeerConnector;
import ch.clops.fmaze.eventsource.EventSourceConnector;
import ch.clops.fmaze.network.ServerSocket;

import java.util.concurrent.CompletableFuture;

public class Server {

    public static void main(String[] args) throws Exception {

        // keeps all connected peers
        PeerRegistry peerRegistry = new PeerRegistry();

        // listen for peer connections
        CompletableFuture<Void> peerFuture = CompletableFuture.runAsync(() ->
            ServerSocket.listen(9099, new PeerConnector(peerRegistry))
        );

        // receives event source events
        ServerSocket.listen(9090, new EventSourceConnector(new EventProcessor(peerRegistry)));

        peerFuture.cancel(false);

        peerRegistry.closeAll();
    }
}
