package chervotkin.dev.eventmanager.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldChangeInteger {
    private Integer oldField;
    private Integer newField;

    public FieldChangeInteger() {}

    @JsonCreator
    public FieldChangeInteger(@JsonProperty("oldField") Integer oldField,
                              @JsonProperty("newField") Integer newField) {
        this.oldField = oldField;
        this.newField = newField;
    }

    public Integer getOldField() { return oldField; }
    public void setOldField(Integer oldField) { this.oldField = oldField; }
    public Integer getNewField() { return newField; }
    public void setNewField(Integer newField) { this.newField = newField; }
}
