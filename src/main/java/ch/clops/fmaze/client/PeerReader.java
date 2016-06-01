package ch.clops.fmaze.client;

import ch.clops.fmaze.network.Peer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rx.functions.Action1;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PeerReader implements Action1<Peer> {

    private final PeerRegistry registry;

    @Override
    public void call(Peer peer) {

        // first line contains the peer id
        peer.read()
            .first()
            .doOnNext(id -> this.registry.onPeerConnected(id, peer))
            .subscribe();
    }
}
