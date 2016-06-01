package ch.clops.fmaze;

import ch.clops.fmaze.client.PeerRegistry;
import ch.clops.fmaze.events.EventProcessor;
import ch.clops.fmaze.client.PeerConnector;
import ch.clops.fmaze.eventsource.EventSourceConnector;
import ch.clops.fmaze.network.ServerSocket;
import rx.schedulers.Schedulers;

import java.util.concurrent.CompletableFuture;

public class Server {

    public static void main(String[] args) throws Exception {

        // keeps all connected peers
        PeerRegistry peerRegistry = new PeerRegistry();

        PeerConnector peerConnector = new PeerConnector(peerRegistry);

        EventSourceConnector eventSourceConnector = new EventSourceConnector(new EventProcessor(peerRegistry));

        ServerSocket.listen(9099)
            .doOnNext(peerConnector)
            .subscribeOn(Schedulers.newThread())
            .subscribe();

        ServerSocket.listen(9090)
            .first()
            .doOnNext(eventSourceConnector)
            .doOnCompleted(() -> peerRegistry.closeAll())
            .subscribe();
    }
}
