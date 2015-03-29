package ch.clops.fmaze.events;

import ch.clops.fmaze.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.PriorityBlockingQueue;

public class EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

    private final PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>();

    private Client nullClient = new Client("0", null) {
        @Override
        public void write(String message) {

            logger.info("Message ignored");
        }
    };

    private Map<String, Client> clientMap = new HashMap<>();
    
    public void on(Optional<Event> event) {

        event.ifPresent(queue::add);
    }

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

    public void onFollow(Event event) {

        // add follower
        // notify followed client
        Client followed = this.clientMap.get(event.to);
        Client follower = this.clientMap.get(event.from);

        followed.addFollower(follower);
    }

    public void onBroadcast(Event event) {

        //notify all clients
        this.clientMap.values().forEach(c -> c.write(event.raw));
    }

    public void onUnfollow(Event event) {

        Client follower = this.clientMap.getOrDefault(event.from, nullClient);
        Client followed = this.clientMap.get(event.to);

        followed.removeFollower(follower);
    }

    public void onPrivate(Event event) {

        // notify target user
        clientMap.getOrDefault(event.to, nullClient).write(event.raw);
    }

    public void onStatusUpdate(Event event) {

        Client client = this.clientMap.get(event.from);

        client.broadcastToFollowers(event.raw);
    }

    public void on(Client client) {

        logger.info("New client {}", client.getID());
        this.clientMap.put(client.getID(), client);
    }
}
