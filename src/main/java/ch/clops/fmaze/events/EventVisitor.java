package ch.clops.fmaze.events;

import ch.clops.fmaze.Client;
import ch.clops.fmaze.ClientRegistry;
import ch.clops.fmaze.network.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by rlorca on 29/03/15.
 */
public class EventVisitor {

    private static final Logger logger = LoggerFactory.getLogger(EventOrder.class);

    private final HashMap<String, Client> clientMap = new HashMap<>();

    private final ClientRegistry clientRegistry;

    public EventVisitor(ClientRegistry registry) {
        this.clientRegistry = registry;
    }

    public void onFollow(Event event) {

        Client f = this.clientMap.get(event.to);

        if(f == null) {
            f = new Client(event.to);
            this.clientMap.put(event.to, f);
        }
        f.addFollower(event.from);

        this.clientRegistry.write(event.to, event.raw);
    }

    public void onBroadcast(Event event) {

        this.clientRegistry.write(event.raw);
    }

    public void onUnfollow(Event event) {

        ifClient(event.to, f -> f.removeFollower(event.from));
    }

    public void onPrivate(Event event) {

        this.clientRegistry.write(event.to, event.raw);
    }

    public void onStatusUpdate(Event event) {

        ifClient(event.from, from -> {
            from.forEachFollower(followerID -> {
                this.clientRegistry.write(followerID, event.raw);
            });
        });
    }

    private Optional<Client> op(String id) {

        Client c = this.clientMap.get(id);
        return c == null ? Optional.<Client>empty() : Optional.<Client>of(c);
    }

    private void ifClient(String id, Consumer<Client> f) {

        op(id).ifPresent(f);
    }
}
