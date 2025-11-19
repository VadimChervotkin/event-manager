package chervotkin.dev.eventmanager.events.domain;


import chervotkin.dev.eventmanager.kafka.dto.EventChangeKafkaMessage;

public class EventChangedDomainEvent {
    private final EventChangeKafkaMessage message;

    public EventChangedDomainEvent(EventChangeKafkaMessage message) {
        this.message = message;
    }

    public EventChangeKafkaMessage getMessage() {
        return message;
    }
}
