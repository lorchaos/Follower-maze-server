package ch.clops.fmaze.events;

import ch.clops.fmaze.client.PeerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RequiredArgsConstructor
public class EventProcessor {

    private final ClientGraph graph = new ClientGraph();

    private final PeerRegistry peerRegistry;

    public void on(FollowEvent event) {

        this.graph.addFollower(event.to, event.from);

        this.peerRegistry.write(event.to, event.raw);
    }

    public void on(BroadcastEvent event) {

        this.peerRegistry.broadcast(event.raw);
    }

    public void on(UnfollowEvent event) {

        this.graph.removeFollower(event.to, event.from);
    }

    public void on(PrivateMessageEvent event) {

        this.peerRegistry.write(event.to, event.raw);
    }

    public void on(StatusUpdateEvent event) {

        this.graph.forEachFollower(event.from, followerID ->
                this.peerRegistry.write(followerID, event.raw)
            );
    }
}
