package chervotkin.dev.eventmanager.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class FieldChangeLong {
    private Long oldField;
    private Long newField;

    public FieldChangeLong() {}

    @JsonCreator
    public FieldChangeLong(@JsonProperty("oldField") Long oldField,
                              @JsonProperty("newField") Long newField) {
        this.oldField = oldField;
        this.newField = newField;
    }

    public Long getOldField() { return oldField; }
    public void setOldField(Long oldField) { this.oldField = oldField; }
    public Long getNewField() { return newField; }
    public void setNewField(Long newField) { this.newField = newField; }
}
