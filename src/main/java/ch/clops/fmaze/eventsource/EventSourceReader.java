package ch.clops.fmaze.eventsource;

import ch.clops.fmaze.events.BaseEvent;
import ch.clops.fmaze.events.EventSorter;
import ch.clops.fmaze.events.EventParser;
import ch.clops.fmaze.network.Peer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

@Slf4j
public class EventSourceReader {

    public static Observable<BaseEvent> eventObservable(Peer peer) {

        EventSorter sorter = new EventSorter();
        EventParser parser = new EventParser();

        return peer.read()
            .map(parser::parse)
            .flatMap(sorter::on);
    }
}
