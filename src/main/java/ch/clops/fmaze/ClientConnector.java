package ch.clops.fmaze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Created by rlorca on 26/03/15.
 */
public class ClientConnector implements Consumer<Socket> {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnector.class);

    private final EventHandler handler;

    ClientConnector(EventHandler handler) {
        this.handler = handler;
    }

    @Override
    public void accept(Socket socket) {

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String clientID = in.readLine();

            Client client = new Client(clientID, out);

            this.handler.on(client);

        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
