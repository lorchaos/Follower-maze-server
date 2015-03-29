package ch.clops.fmaze.events;

import ch.clops.fmaze.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by rlorca on 29/03/15.
 */
public class EventVisitor {

    private static final Logger logger = LoggerFactory.getLogger(EventOrder.class);

    private final ConcurrentHashMap<String, Client> clientMap = new ConcurrentHashMap<>();

    public void onFollow(Event event) {

        ifClient(event.to, followed -> {

            followed.addFollower(event.from);
            followed.write(event.raw);
        });
    }

    public void onBroadcast(Event event) {

        //notify all clients
        this.clientMap.values().forEach(c -> c.write(event.raw));
    }

    public void onUnfollow(Event event) {

        ifClient(event.to, f -> {
            f.removeFollower(event.from);
        });
    }

    public void onPrivate(Event event) {

        ifClient(event.to, c -> {
            c.write(event.raw);
        });
    }

    public void onStatusUpdate(Event event) {

        ifClient(event.from, from -> {

            from.forEachFollower(followerID -> {
                ifClient(followerID, follower -> follower.write(event.raw));
            });
        });
    }

    public void on(Client client) {

        logger.info("New client {}", client.getID());
        this.clientMap.put(client.getID(), client);
    }

    private Optional<Client> op(String id) {

        Client c = this.clientMap.get(id);
        return c == null ? Optional.<Client>empty() : Optional.<Client>of(c);
    }

    private void ifClient(String id, Consumer<Client> f) {

        op(id).ifPresent(f);
    }
}
