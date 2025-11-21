package chervotkin.dev.eventmanager.kafka;


import chervotkin.dev.eventmanager.kafka.dto.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventChangeProducer {
    private final Logger log = LoggerFactory.getLogger(EventChangeProducer.class);
    private final KafkaTemplate<String, EventChangeKafkaMessage> kafkaTemplate;
    private final String topic = "events.changes";

    public EventChangeProducer(KafkaTemplate<String, EventChangeKafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(EventChangeKafkaMessage msg) {
        log.info("Publishing event-change to topic {} for eventId={}", topic, msg.getEventId());
        kafkaTemplate.send(topic, String.valueOf(msg.getEventId()), msg);
    }
}
