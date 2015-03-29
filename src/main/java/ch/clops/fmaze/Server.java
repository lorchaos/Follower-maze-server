package ch.clops.fmaze;

import ch.clops.fmaze.events.EventOrder;
import ch.clops.fmaze.events.EventVisitor;
import ch.clops.fmaze.network.ClientConnector;
import ch.clops.fmaze.network.EventSourceConnector;
import ch.clops.fmaze.network.ServerSocket;

import java.util.concurrent.CompletableFuture;

/**
 * Created by rlorca on 26/03/15.
 */
public class Server {

    public static void main(String[] args) throws Exception{

        EventVisitor handler = new EventVisitor();

        // receives event source events
        CompletableFuture<Void> evFuture = new ServerSocket(9090).listen(new EventSourceConnector(handler));

        // listen for clients
        CompletableFuture<Void> clientFuture = new ServerSocket(9099).listen(new ClientConnector(handler));

        // waits for the event store completion
        evFuture.get();

        clientFuture.cancel(true);
    }
}
