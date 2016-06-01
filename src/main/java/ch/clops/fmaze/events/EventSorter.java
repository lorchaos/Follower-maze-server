package ch.clops.fmaze.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.*;
import rx.Observable;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class EventSorter {

    private final PriorityQueue<BaseEvent> queue = new PriorityQueue<>();

    private int expectedSequence = 1;

    public Observable<BaseEvent> on(Optional<? extends BaseEvent> event) {

        log.info("Received event {}", event);

        event.ifPresent(queue::add);

        return Observable.create(subscriber -> {

            while(true) {
              final BaseEvent head = queue.peek();

              if((head != null) && (head.sequence == this.expectedSequence)) {
                this.expectedSequence++;
                subscriber.onNext(queue.poll());
              } else {
                subscriber.onCompleted();
                return;
              }
            }
        });
    }

    private BaseEvent processQueue() {



        return null;
    }
}
