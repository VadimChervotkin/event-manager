package chervotkin.dev.eventmanager.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class FieldChangeDecimal {
    private BigDecimal oldField;
    private BigDecimal newField;

    public FieldChangeDecimal() {}

    @JsonCreator
    public FieldChangeDecimal(@JsonProperty("oldField") BigDecimal oldField,
                              @JsonProperty("newField") BigDecimal newField) {
        this.oldField = oldField;
        this.newField = newField;
    }

    public BigDecimal getOldField() { return oldField; }
    public void setOldField(BigDecimal oldField) { this.oldField = oldField; }
    public BigDecimal getNewField() { return newField; }
    public void setNewField(BigDecimal newField) { this.newField = newField; }
}
