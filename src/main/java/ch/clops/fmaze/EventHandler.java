package ch.clops.fmaze;

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

    private Map<String, Set<Client>> followers = new HashMap<>();

    public void on(Event event) {

        queue.add(event);
    }

    public CompletableFuture<Void> process() {

        return CompletableFuture.runAsync(() -> {

            int expected = 1;

            while(true) {
                Event head = queue.peek();

                if (head != null && head.sequence == expected) {
                    expected++;
                    process(queue.poll());
                }
            }
        });
    }

    private void process(Event event) {

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
        if(followed != null) {

            Client follower = this.clientMap.get(event.from);

            this.followers.get(event.to).add(follower);

            followed.write(event.raw);
        }
    }

    public void onBroadcast(Event event) {

        //notify all clients
        this.clientMap.values().forEach(c -> c.write(event.raw));
    }

    public void onUnfollow(Event event) {

        Client client = this.clientMap.getOrDefault(event.from, nullClient);

        // remove follower, no notification
        Set<Client> clients = this.followers.get(event.to);
        if(clients != null) {
            clients.remove(client);
        }
    }

    public void onPrivate(Event event) {

        // notify target user
        clientMap.getOrDefault(event.to, nullClient).write(event.raw);
    }

    public void onStatusUpdate(Event event) {

        // notify followers
        this.followers.getOrDefault(event.from, Collections.emptySet()).forEach(c -> c.write(event.raw));
    }

    public void on(Client client) {

        logger.info("New client {}", client.getID());
        this.clientMap.put(client.getID(), client);
        this.followers.put(client.getID(), new HashSet<>());
    }
}
