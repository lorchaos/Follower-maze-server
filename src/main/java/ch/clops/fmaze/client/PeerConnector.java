package ch.clops.fmaze.client;

import ch.clops.fmaze.network.Peer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rx.functions.Action1;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PeerConnector implements Action1<Peer> {

    private final PeerRegistry registry;

    @Override
    public void call(Peer peer) {
        Optional<String> id = peer.read().findFirst();
        if(id.isPresent()) {
            this.registry.onPeerConnected(id.get(), peer);
        } else {
            log.warn("Unable to get client ID");
        }
    }
}
