package ch.clops.fmaze;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Created by rlorca on 26/03/15.
 */
public class Server {

    public static void main(String[] args) throws Exception{

        EventHandler handler = new EventHandler();

        CompletableFuture<Void> evFuture = new ServerSocket(9090).listen(new EventSourceConnector(handler));

        CompletableFuture<Void> clientFuture = new ServerSocket(9099).listen(new ClientConnector(handler));

        CompletableFuture<Void> h = handler.process();


        CompletableFuture.allOf(evFuture, clientFuture, h).get();
    }
}
