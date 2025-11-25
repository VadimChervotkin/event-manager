package chervotkin.dev.eventmanager.kafka.dto;

import chervotkin.dev.eventmanager.events.api.EventStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class FieldChangeStatus {
    private EventStatus oldField;
    private EventStatus newField;

    public FieldChangeStatus() {}

    @JsonCreator
    public FieldChangeStatus(@JsonProperty("oldField") EventStatus oldField,
                             @JsonProperty("newField") EventStatus newField) {
        this.oldField = oldField;
        this.newField = newField;
    }

    public EventStatus getOldField() { return oldField; }
    public void setOldField(EventStatus oldField) { this.oldField = oldField; }
    public EventStatus getNewField() { return newField; }
    public void setNewField(EventStatus newField) { this.newField = newField; }
}
