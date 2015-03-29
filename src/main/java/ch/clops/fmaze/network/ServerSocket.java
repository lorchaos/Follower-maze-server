package ch.clops.fmaze.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created by rlorca on 26/03/15.
 */
public class ServerSocket {

    private static final Logger logger = LoggerFactory.getLogger(EventSourceConnector.class);

    private final int port;

    public ServerSocket(int port) {

        this.port = port;
    }

    public CompletableFuture<Void> listen(final Connector connector) throws IOException {

        java.net.ServerSocket socket = new java.net.ServerSocket(this.port);

        return CompletableFuture.runAsync(() -> {

            try {
                while(connector.newPeer(new Peer(socket.accept())));
                logger.info("Connector on port {} finished.", this.port);

            } catch (Exception e) {
                logger.warn("Stopping server in port " + this.port, e);
            }


        });
    }
}
