package ch.clops.fmaze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by rlorca on 26/03/15.
 */
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final BufferedWriter writer;
    private String id;

    public Client(String id, BufferedWriter writer) {

        this.id = id;
        this.writer = writer;
    }


    public void write(String message) {

        logger.info("Writing {} : {}", this.id, message);
        try {
            this.writer.write(message);
            this.writer.write("\r\n");
            this.writer.flush();
        } catch (IOException e) {
            logger.error("Unable to write to client: " + this.id, e);
        }
    }

    public String getID() {
        return this.id;
    }
}
