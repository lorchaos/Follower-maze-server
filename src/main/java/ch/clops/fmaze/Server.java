package ch.clops.fmaze;

import ch.clops.fmaze.client.ClientRegistry;
import ch.clops.fmaze.events.EventProcessor;
import ch.clops.fmaze.client.ClientConnector;
import ch.clops.fmaze.eventsource.EventSourceConnector;
import ch.clops.fmaze.network.ServerSocket;

import java.util.concurrent.CompletableFuture;

/**
 * Created by rlorca on 26/03/15.
 */
public class Server {

    public static void main(String[] args) throws Exception{

        ClientRegistry registry = new ClientRegistry();

        EventProcessor handler = new EventProcessor(registry);

        // receives event source events
        CompletableFuture<Void> evFuture = new ServerSocket(9090).listen(new EventSourceConnector(handler));

        // listen for clients
        CompletableFuture<Void> clientFuture = new ServerSocket(9099).listen(new ClientConnector(registry));

        // waits for the event store completion
        evFuture.get();

        clientFuture.cancel(false);
    }
}
