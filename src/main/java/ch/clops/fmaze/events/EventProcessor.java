package ch.clops.fmaze.events;

import ch.clops.fmaze.client.Client;
import ch.clops.fmaze.client.ClientRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;

/**
 * Created by rlorca on 29/03/15.
 */
public class EventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventSorter.class);

    private final HashMap<String, Client> clientMap = new HashMap<>();

    private final ClientRegistry clientRegistry;

    public EventProcessor(ClientRegistry registry) {
        this.clientRegistry = registry;
    }

    public void on(FollowEvent event) {

        getClient(event.to).orElseGet(() -> {

                    Client from = new Client(event.to);
                    this.clientMap.put(event.to, from);
                    return from;

                }).addFollower(event.from);


        this.clientRegistry.write(event.to, event.raw);
    }

    public void on(BroadcastEvent event) {

        this.clientRegistry.write(event.raw);
    }

    public void on(UnfollowEvent event) {

        getClient(event.to).ifPresent(f -> f.removeFollower(event.from));
    }

    public void on(PrivateMessageEvent event) {

        this.clientRegistry.write(event.to, event.raw);
    }

    public void on(StatusUpdateEvent event) {

        getClient(event.from).ifPresent(from -> {
            from.forEachFollower(followerID -> {
                this.clientRegistry.write(followerID, event.raw);
            });
        });
    }

    private Optional<Client> getClient(String id) {
        return Optional.ofNullable(this.clientMap.get(id));
    }
}
