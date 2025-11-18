package chervotkin.dev.eventmanager.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class FieldChangeDateTime {
    private LocalDateTime oldField;
    private LocalDateTime newField;

    public FieldChangeDateTime() {}

    @JsonCreator
    public FieldChangeDateTime(@JsonProperty("oldField") LocalDateTime oldField,
                               @JsonProperty("newField") LocalDateTime newField) {
        this.oldField = oldField;
        this.newField = newField;
    }

    public LocalDateTime getOldField() { return oldField; }
    public void setOldField(LocalDateTime oldField) { this.oldField = oldField; }
    public LocalDateTime getNewField() { return newField; }
    public void setNewField(LocalDateTime newField) { this.newField = newField; }
}
