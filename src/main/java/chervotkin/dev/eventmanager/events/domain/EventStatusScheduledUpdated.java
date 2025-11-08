package chervotkin.dev.eventmanager.events.domain;

import chervotkin.dev.eventmanager.events.api.EventStatus;
import chervotkin.dev.eventmanager.events.db.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class EventStatusScheduledUpdated {

    private final static Logger log = LoggerFactory.getLogger(EventStatusScheduledUpdated.class);

    private final EventRepository eventRepository;

    public EventStatusScheduledUpdated(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "${event.status.cron}")
    public void updateEventStatus() {
        log.info("EventStatusScheduledUpdated started");

        var startedEvents = eventRepository.findStartedEventsWithStatus(EventStatus.WAIT_START);
        startedEvents.forEach(eventId ->
            eventRepository.changeEventStatus(eventId, EventStatus.STARTED)
        );

        var endedEvents = eventRepository.findEndedEventsWithStatus(EventStatus.STARTED);
        endedEvents.forEach(eventId ->
                eventRepository.changeEventStatus(eventId, EventStatus.FINISHED));
    }
}
