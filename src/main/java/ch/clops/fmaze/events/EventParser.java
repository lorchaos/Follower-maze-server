package ch.clops.fmaze.events;

import java.util.Optional;

/**
 * Created by rlorca on 25/03/15.
 */
public class EventParser {

    public Optional<Event> parse(String raw) {

        final String[] fields = raw.split("\\|");

        int eventSequence = Integer.parseInt(fields[0]);

        return EventType.fromSymbol(fields[1]).map(type -> {

            String eventType = fields[1];
            String to = null, from = null;

            if (fields.length > 2) {
                from = fields[2];
            }

            if (fields.length > 3) {
                to = fields[3];
            }

            return new Event(raw, eventSequence, type, from, to);
        });
    }


}
