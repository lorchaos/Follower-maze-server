package ch.clops.fmaze;

import ch.clops.fmaze.network.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Created by rlorca on 26/03/15.
 */
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private final Peer peer;

    private String id;

    public Client(String id, Peer peer) {

        this.id = id;
        this.peer = peer;
    }

    public void write(String message) {

        logger.info("Writing {} : {}", this.id, message);

        try {
            this.peer.write(message + "\r\n");
        } catch (IOException e) {
            logger.error("Unable to write to client: " + this.id, e);
        }
    }

    public String getID() {
        return this.id;
    }
}
