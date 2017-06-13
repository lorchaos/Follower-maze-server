package ch.clops.fmaze.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Peer {

    private static final Logger logger = LoggerFactory.getLogger(Peer.class);

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    public Peer(Socket socket) throws Exception {
        this.socket = socket;

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public Stream<String> read() {

        Iterator<String> it = new Iterator<String>() {

            public String next;

            @Override
            public boolean hasNext() {

                try {
                    next = in.readLine();
                    return next != null;
                } catch(Exception e) {
                    return false;
                }
            }

            @Override
            public String next() {
                return next;
            }
        };

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }

    public void write(String msg) {

        try {
            this.out.write(msg);
            this.out.write("\r\n");
            this.out.flush();
        } catch (IOException e) {
            logger.error("Unable to write to peer", e);
        }
    }

    public void close() {

        try {
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
