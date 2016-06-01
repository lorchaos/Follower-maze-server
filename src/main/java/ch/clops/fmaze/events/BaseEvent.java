package ch.clops.fmaze.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by rlorca on 25/03/15.
 */

@RequiredArgsConstructor
public abstract class BaseEvent implements Comparable<BaseEvent> {

    public final String raw;
    public final int sequence;

    @Override
    public int compareTo(BaseEvent o) {
        return this.sequence - o.sequence;
    }

    @Override
    public String toString() {
        return this.raw;
    }

    public abstract void process(EventProcessor visitor);
}

abstract class CompleteEvent extends BaseEvent {

    public final String to;
    public final String from;

    CompleteEvent(String raw, int sequence, String from, String to) {
        super(raw, sequence);
        this.to = to;
        this.from = from;
    }
}

class BroadcastEvent extends BaseEvent {

    BroadcastEvent(String raw, int sequence) {
        super(raw, sequence);
    }

    public void process(EventProcessor visitor) {
        visitor.on(this);
    }
}

class StatusUpdateEvent extends BaseEvent {

    public final String from;

    StatusUpdateEvent(String raw, int sequence, String from) {
        super(raw, sequence);
        this.from = from;
    }

    public void process(EventProcessor visitor) {
        visitor.on(this);
    }
}

class FollowEvent extends CompleteEvent {

    FollowEvent(String raw, int sequence, String from, String to) {
        super(raw, sequence, from, to);
    }

    public void process(EventProcessor visitor) {
        visitor.on(this);
    }
}

class PrivateMessageEvent extends CompleteEvent {

    PrivateMessageEvent(String raw, int sequence, String from, String to) {
        super(raw, sequence, from, to);
    }

    public void process(EventProcessor visitor) {
        visitor.on(this);
    }
}


class UnfollowEvent extends CompleteEvent {

    UnfollowEvent(String raw, int sequence, String from, String to) {
        super(raw, sequence, from, to);
    }

    public void process(EventProcessor visitor) {
        visitor.on(this);
    }
}