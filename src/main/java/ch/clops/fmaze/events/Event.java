package ch.clops.fmaze.events;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Created by rlorca on 25/03/15.
 */
public class Event implements Comparable<Event> {

    public final String raw;
    public final int sequence;
    public final String to;
    public final String from;
    public final EventType type;

    Event(String raw, int sequence, EventType type, String from, String to) {
        this.raw = raw;
        this.sequence = sequence;
        this.to = to;
        this.from = from;
        this.type = type;
    }

    @Override
    public int compareTo(Event o) {
        return this.sequence - o.sequence;
    }
}

enum EventType {

    FOLLOW("F"),
    UNFOLLOW("U"),
    PRIVATE_MESSAGE("P"),
    BROADCAST("B"),
    STATUS_UPDATE("S");


    private final String symbol;

    EventType(String symbol) {
        this.symbol = symbol;
    }

    static Optional<EventType> fromSymbol(String symbol) {

        for(EventType e : EnumSet.allOf(EventType.class)) {

            if(e.symbol.equals(symbol))
                return Optional.of(e);
        }

        return Optional.empty();
    }

}