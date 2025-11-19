package chervotkin.dev.eventmanager.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EventChangeKafkaMessage {

    private Long eventId;
    private List<Long> users;
    private Long ownerId;
    private Long changedById;

    private FieldChangeString name;
    private FieldChangeInteger maxPlaces;
    private FieldChangeDateTime date;
    private FieldChangeInteger cost;
    private FieldChangeInteger duration;
    private FieldChangeLong locationId;
    private FieldChangeStatus status;

    public EventChangeKafkaMessage() {}

    @JsonCreator
    public EventChangeKafkaMessage(
            @JsonProperty("eventId") Long eventId,
            @JsonProperty("users") List<Long> users,
            @JsonProperty("ownerId") Long ownerId,
            @JsonProperty("changedById") Long changedById,
            @JsonProperty("name") FieldChangeString name,
            @JsonProperty("maxPlaces") FieldChangeInteger maxPlaces,
            @JsonProperty("date") FieldChangeDateTime date,
            @JsonProperty("cost") FieldChangeInteger cost,
            @JsonProperty("duration") FieldChangeInteger duration,
            @JsonProperty("locationId") FieldChangeLong locationId,
            @JsonProperty("status") FieldChangeStatus status
    ) {
        this.eventId = eventId;
        this.users = users;
        this.ownerId = ownerId;
        this.changedById = changedById;
        this.name = name;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getChangedById() {
        return changedById;
    }

    public void setChangedById(Long changedById) {
        this.changedById = changedById;
    }

    public FieldChangeString getName() {
        return name;
    }

    public void setName(FieldChangeString name) {
        this.name = name;
    }

    public FieldChangeInteger getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(FieldChangeInteger maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public FieldChangeDateTime getDate() {
        return date;
    }

    public void setDate(FieldChangeDateTime date) {
        this.date = date;
    }

    public FieldChangeInteger getCost() {
        return cost;
    }

    public void setCost(FieldChangeInteger cost) {
        this.cost = cost;
    }

    public FieldChangeInteger getDuration() {
        return duration;
    }

    public void setDuration(FieldChangeInteger duration) {
        this.duration = duration;
    }

    public FieldChangeLong getLocationId() {
        return locationId;
    }

    public void setLocationId(FieldChangeLong locationId) {
        this.locationId = locationId;
    }

    public FieldChangeStatus getStatus() {
        return status;
    }

    public void setStatus(FieldChangeStatus status) {
        this.status = status;
    }
}
