package ch.clops.fmaze.network;

import ch.clops.fmaze.eventsource.EventSourceConnector;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

import java.io.IOException;

@Slf4j
public class ServerSocket {

    public static Observable<Peer> listen(final int port) {

        Func0<java.net.ServerSocket> factory = () -> {
            try {
                return new java.net.ServerSocket(port);
            } catch (IOException e) {
                throw new RuntimeException("Unable to open server connection.", e);
            }
        };

        Action1<java.net.ServerSocket> destroy = (s) -> {
            try {
                s.close();
            } catch (IOException e) {
                //log.warn("Unable to close server socket", e);
            }
        };


        return Observable.using(factory, ServerSocket::accept, destroy);
    }

    private static Observable<Peer> accept(java.net.ServerSocket socket) {

        return Observable.create(subscriber -> {

            //logger.info("Socket listening on port {}", port);

            try {
                while (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(new Peer(socket.accept()));
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
