package ch.clops.fmaze;

import java.util.Optional;

/**
 * Created by rlorca on 25/03/15.
 */
public class EventParser {

    private final EventHandler handler;

    public EventParser(EventHandler handler) {
        this.handler = handler;
    }

    public void parse(String raw) {

        final String[] fields = raw.split("\\|");

        int eventSequence = Integer.parseInt(fields[0]);
        String eventType = fields[1];



        if (eventType.equals("B")) {
            handler.on(new BroadcastEvent(raw, eventSequence));
        } else if(eventType.equals("S")) {
            String from = fields[2];
            handler.on(new StatusUpdateEvent(raw, eventSequence, from));
        } else if (eventType.equals("F") || eventType.equals("U") || eventType.equals("P")) {

            String from = fields[2];
            String to = fields[3];

            if (eventType.equals("F")){
                handler.on(new FollowEvent(raw, eventSequence, from, to));
            } else if(eventType.equals("P")) {
                handler.on(new PrivateMessageEvent(raw, eventSequence, from, to));
            } else if(eventType.equals("U")) {
                handler.on(new UnfollowEvent(raw, eventSequence, from, to));
            }
        }
    }


}
