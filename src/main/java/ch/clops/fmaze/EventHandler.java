package ch.clops.fmaze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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

    public void on(FollowEvent event) {

        // add follower
        // notify followed client
        Client followed = this.clientMap.get(event.toUser);
        if(followed != null) {

            Client follower = this.clientMap.get(event.fromUser);

            this.followers.get(event.toUser).add(follower);

            followed.write(event.raw);
        }
    }

    public void on(BroadcastEvent event) {

        //notify all clients
        this.clientMap.values().forEach(c -> c.write(event.raw));
    }

    public void on(UnfollowEvent event) {

        Client client = this.clientMap.getOrDefault(event.fromUser, nullClient);

        // remove follower, no notification
        Set<Client> clients = this.followers.get(event.toUser);
        if(clients != null) {
            clients.remove(client);
        }
    }

    public void on(PrivateMessageEvent event) {

        // notify target user
        clientMap.getOrDefault(event.toUser, nullClient).write(event.raw);
    }

    public void on(StatusUpdateEvent event) {

        // notify followers
        this.followers.getOrDefault(event.fromUser, Collections.emptySet()).forEach(c -> c.write(event.raw));
    }

    public void on(Client client) {

        logger.info("New client {}", client.getID());
        this.clientMap.put(client.getID(), client);
        this.followers.put(client.getID(), new HashSet<>());
    }
}
