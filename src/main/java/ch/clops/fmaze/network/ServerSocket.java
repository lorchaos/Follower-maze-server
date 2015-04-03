package ch.clops.fmaze.network;

import ch.clops.fmaze.eventsource.EventSourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ServerSocket {

    private static final Logger logger = LoggerFactory.getLogger(EventSourceConnector.class);

    private final int port;

    public ServerSocket(int port) {

        this.port = port;
    }

    public CompletableFuture<Void> listen(final Connector connector) {

        return CompletableFuture.runAsync(() -> {

            try (java.net.ServerSocket socket = new java.net.ServerSocket(this.port)) {

                while (connector.newPeer(new Peer(socket.accept()))) ;

            } catch (Exception e) {
                logger.error("Error on connector, port " + this.port, e);
            } finally {
                connector.stop();
                logger.info("Connector on port {} finished.", this.port);
            }
        });
    }
}
