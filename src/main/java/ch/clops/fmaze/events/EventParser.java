package ch.clops.fmaze.events;

import java.util.Optional;

/**
 * Created by rlorca on 25/03/15.
 */
public class EventParser {

    public Optional<? extends BaseEvent> parse(String raw) {

        final String[] fields = raw.split("\\|");

        if(fields.length < 2) {
           return Optional.empty();
        }

        final int eventSequence = Integer.parseInt(fields[0]);

        final char type = fields[1].charAt(0);
        final int sequence = Integer.parseInt(fields[0]);

        String from = null, to = null;

        if (fields.length > 2) {
            from = fields[2];
        }

        if (fields.length > 3) {
            to = fields[3];
        }

        switch(type) {
            case 'B': return Optional.of(new BroadcastEvent(raw, sequence));

            case 'S': return Optional.of(new StatusUpdateEvent(raw, sequence, from));

            case 'F': return Optional.of(new FollowEvent(raw, sequence, from, to));

            case 'U': return Optional.of(new UnfollowEvent(raw, sequence, from, to));

            case 'P': return Optional.of(new PrivateMessageEvent(raw, sequence, from, to));

            default: return Optional.empty();
        }
    }
}
