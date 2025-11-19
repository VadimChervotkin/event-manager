package chervotkin.dev.eventmanager.events.domain;


import chervotkin.dev.eventmanager.kafka.EventChangeProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
public class EventChangedDomainEventListener {

    private final EventChangeProducer producer;

    public EventChangedDomainEventListener(EventChangeProducer producer) {
        this.producer = producer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EventChangedDomainEvent event) {
        producer.send(event.getMessage());
    }
}
