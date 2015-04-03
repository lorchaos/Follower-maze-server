package ch.clops.fmaze.network;

import ch.clops.fmaze.eventsource.EventSourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ServerSocket {

    private static final Logger logger = LoggerFactory.getLogger(EventSourceConnector.class);

    public static void listen(final int port, final Connector connector) {

        try (java.net.ServerSocket socket = new java.net.ServerSocket(port)) {

            logger.info("Socket listening on port {}", port);

            while (connector.newPeer(new Peer(socket.accept()))) ;

        } catch (Exception e) {
            logger.error("Error on connector, port " + port, e);
        } finally {
            logger.info("Connector on port {} finished.", port);
        }
    }
}
