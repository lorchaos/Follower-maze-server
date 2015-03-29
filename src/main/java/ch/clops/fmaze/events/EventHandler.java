package ch.clops.fmaze.events;

import ch.clops.fmaze.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Consumer;

public class EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

    private final PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>();

    private final HashMap<String, Client> clientMap = new HashMap<>();

    public CompletableFuture<Void> process() {

        return CompletableFuture.runAsync(() -> {

            int expected = 1;

            while(true) {
                Event head = queue.peek();

                if (head != null) {
                    if (head.sequence == expected) {
                        expected++;
                        process(queue.poll());
                    } else {
                        logger.info("Event {} received, waiting for {}", head.sequence, expected);
                    }
                }
            }
        });
    }

    private void process(Event event) {

        logger.info("Processing event {}, {}", event.sequence, event.raw);

        switch (event.type) {

            case BROADCAST:
                onBroadcast(event);
                break;

            case FOLLOW:
                onFollow(event);
                break;

            case PRIVATE_MESSAGE:
                onPrivate(event);
                break;
            case STATUS_UPDATE:
                onStatusUpdate(event);
                break;

            case UNFOLLOW:
                onUnfollow(event);
                break;
        }
    }

    public void on(Optional<Event> event) {

        event.ifPresent(queue::add);
    }

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
