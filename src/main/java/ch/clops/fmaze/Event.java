package ch.clops.fmaze;

/**
 * Created by rlorca on 25/03/15.
 */
abstract public class Event {

    public final String raw;
    public final int sequence;

    Event(String raw, int sequence) {
        this.raw = raw;
        this.sequence = sequence;
    }
}

abstract class TargetedEvent extends Event {

    public final String fromUser;
    public final String toUser;


    TargetedEvent(String raw, int sequence, String fromUser, String toUser) {

        super(raw, sequence);
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}

class BroadcastEvent extends Event {

    BroadcastEvent(String raw, int sequence) {
        super(raw, sequence);
    }
}

class StatusUpdateEvent extends Event {

    public final String fromUser;

    StatusUpdateEvent(String raw, int sequence, String fromUser) {
        super(raw, sequence);
        this.fromUser = fromUser;
    }
}

class FollowEvent extends TargetedEvent {

    FollowEvent(String raw, int sequence, String fromUser, String toUser) {

        super(raw, sequence, fromUser, toUser);
    }
}


class UnfollowEvent extends TargetedEvent {

    UnfollowEvent(String raw, int sequence, String fromUser, String toUser) {
        super(raw, sequence, fromUser, toUser);
    }
}

class PrivateMessageEvent extends TargetedEvent {

    PrivateMessageEvent(String raw, int sequence, String fromUser, String toUser) {
        super(raw, sequence, fromUser, toUser);
    }
}