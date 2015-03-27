package ch.clops.fmaze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.function.Consumer;

public class EventSourceConnector implements Consumer<Socket> {

    private static final Logger logger = LoggerFactory.getLogger(EventSourceConnector.class);

    private final EventParser parser;
    private final EventHandler handler;

    public EventSourceConnector(EventHandler handler) {
        this.parser = new EventParser();
        this.handler = handler;
    }


    @Override
    public void accept(Socket socket) {

        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"))) {

            String line;

            while((line = in.readLine()) != null){

                handleEvent(line);
            }

        } catch (IOException e) {
            logger.error("", e);
        }

        logger.info("Connection closed.");
    }

    private void handleEvent(String s) {

        logger.info("Event received {}", s);

        this.parser.parse(s).ifPresent(this.handler::on);
    }
}
