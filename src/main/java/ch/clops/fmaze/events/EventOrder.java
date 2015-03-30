package ch.clops.fmaze.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EventOrder {

    private static final Logger logger = LoggerFactory.getLogger(EventOrder.class);


    private final PriorityQueue<Event> queue = new PriorityQueue<>();

    private final EventVisitor visitor;

    private int expectedSequence = 1;

    public EventOrder(EventVisitor visitor) {
        this.visitor = visitor;
    }

    public void on(Optional<Event> event) {

        logger.info("Received event {}", event);

        event.ifPresent(queue::add);

        while(processQueue());
    }

    private boolean processQueue() {

        final Event head = queue.peek();

        if((head != null) && (head.sequence == this.expectedSequence)) {

            this.expectedSequence++;
            Event event = queue.poll();
            event.process(this.visitor);

            logger.info("Processing event {}", event);

            return true;
        }

        return false;
    }
}
