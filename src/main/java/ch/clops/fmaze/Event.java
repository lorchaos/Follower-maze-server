package ch.clops.fmaze;

/**
 * Created by rlorca on 25/03/15.
 */
public class Event {

    public final String raw;

    public final String type;
    public final int sequence;
    public final int fromUser;
    public final int toUser;


    Event(String raw) {
        this.raw = raw;

        String[] fields = raw.split("\\|");

        this.sequence = Integer.parseInt(fields[0]);
        this.type = fields[1];
        this.fromUser = Integer.parseInt(fields[2]);
        this.toUser = Integer.parseInt(fields[3]);
    }
}
