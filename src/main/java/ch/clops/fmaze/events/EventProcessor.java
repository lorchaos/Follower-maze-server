package ch.clops.fmaze.events;

import ch.clops.fmaze.client.PeerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventSorter.class);

    private final ClientGraph graph = new ClientGraph();

    private final PeerRegistry peerRegistry;

    public EventProcessor(PeerRegistry registry) {
        this.peerRegistry = registry;
    }

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
