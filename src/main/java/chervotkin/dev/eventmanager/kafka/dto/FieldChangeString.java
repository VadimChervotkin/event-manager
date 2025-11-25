package chervotkin.dev.eventmanager.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldChangeString {
    private String oldField;
    private String newField;

    public FieldChangeString() {}

    @JsonCreator
    public FieldChangeString(@JsonProperty("oldField") String oldField,
                             @JsonProperty("newField") String newField) {
        this.oldField = oldField;
        this.newField = newField;
    }

    public String getOldField() { return oldField; }
    public void setOldField(String oldField) { this.oldField = oldField; }
    public String getNewField() { return newField; }
    public void setNewField(String newField) { this.newField = newField; }
}
