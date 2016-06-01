package ch.clops.fmaze.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class EventSorter {

    private final PriorityQueue<BaseEvent> queue = new PriorityQueue<>();

    private final EventProcessor visitor;

    private int expectedSequence = 1;

    public int on(Optional<? extends BaseEvent> event) {

        log.info("Received event {}", event);

        event.ifPresent(queue::add);

        while(processQueue());

        return queue.size();
    }

    private boolean processQueue() {

        final BaseEvent head = queue.peek();

        if((head != null) && (head.sequence == this.expectedSequence)) {

            this.expectedSequence++;
            BaseEvent event = queue.poll();
            event.process(this.visitor);

            log.info("Processing event {}", event);

            return true;
        }

        return false;
    }
}
