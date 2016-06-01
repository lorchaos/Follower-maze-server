package ch.clops.fmaze;

import ch.clops.fmaze.client.PeerRegistry;
import ch.clops.fmaze.events.EventProcessor;
import ch.clops.fmaze.client.PeerReader;
import ch.clops.fmaze.eventsource.EventSourceReader;
import ch.clops.fmaze.network.ServerSocket;
import lombok.extern.slf4j.Slf4j;
import rx.Subscription;
import rx.schedulers.Schedulers;

@Slf4j
public class Server {

    public static final int PEER_SOCKET_PORT = 9099;
    public static final int EVENT_SOURCE_PORT = 9090;

    public static void main(String[] args) throws Exception {

        // keeps all connected peers
        PeerRegistry peerRegistry = new PeerRegistry();
        PeerReader peerReader = new PeerReader(peerRegistry);
        EventProcessor eventProcessor = new EventProcessor(peerRegistry);

        Subscription peerSubscription = ServerSocket.listen(PEER_SOCKET_PORT)
            .doOnNext(peerReader)
            .subscribeOn(Schedulers.newThread())
            .subscribe();

        ServerSocket.listen(EVENT_SOURCE_PORT)
            .first()
            .flatMap(EventSourceReader::eventObservable)
            .doOnNext(e -> e.process(eventProcessor))
            .doOnCompleted(() -> peerSubscription.unsubscribe())
            .doOnCompleted(() -> peerRegistry.closeAll())
            .doOnCompleted(() -> log.info("Service terminated"))
            .subscribe();
    }
}
