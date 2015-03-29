package ch.clops.fmaze.network;

import ch.clops.fmaze.Client;
import ch.clops.fmaze.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Created by rlorca on 26/03/15.
 */
public class ClientConnector implements Connector {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnector.class);

    private final EventHandler handler;

    public ClientConnector(EventHandler handler) {
        this.handler = handler;
    }

    @Override
    public Boolean newPeer(Peer peer) {

        String clientID =  peer.read().findFirst().orElseThrow(() -> new RuntimeException("Unable to get client ID"));

        Client client = new Client(clientID, peer);

        this.handler.on(client);

        return true;
    }
}
