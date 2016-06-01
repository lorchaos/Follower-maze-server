package ch.clops.fmaze.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.io.*;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
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

    public Observable<String> read() {

        return Observable.create(subscriber ->  {

            try {
                while (!subscriber.isUnsubscribed()) {

                    String line = in.readLine();
                    if (line == null) {
                        subscriber.onCompleted();
                    } else {
                        subscriber.onNext(line);
                    }
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
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
