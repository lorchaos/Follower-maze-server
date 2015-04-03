package ch.clops.fmaze.events;

import ch.clops.fmaze.client.ClientGraph;
import ch.clops.fmaze.client.PeerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;

public class EventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventSorter.class);

    private final HashMap<String, ClientGraph> clientMap = new HashMap<>();

    private final PeerRegistry peerRegistry;

    public EventProcessor(PeerRegistry registry) {
        this.peerRegistry = registry;
    }

    public void on(FollowEvent event) {

        getClient(event.to).orElseGet(() -> {

            ClientGraph from = new ClientGraph(event.to);
            this.clientMap.put(event.to, from);
            return from;

        }).addFollower(event.from);


        this.peerRegistry.write(event.to, event.raw);
    }

    public void on(BroadcastEvent event) {

        this.peerRegistry.broadcast(event.raw);
    }

    public void on(UnfollowEvent event) {

        getClient(event.to).ifPresent(f -> f.removeFollower(event.from));
    }

    public void on(PrivateMessageEvent event) {

        this.peerRegistry.write(event.to, event.raw);
    }

    public void on(StatusUpdateEvent event) {

        getClient(event.from).ifPresent(from -> {
            from.forEachFollower(followerID -> {
                this.peerRegistry.write(followerID, event.raw);
            });
        });
    }

    private Optional<ClientGraph> getClient(String id) {
        return Optional.ofNullable(this.clientMap.get(id));
    }
}
