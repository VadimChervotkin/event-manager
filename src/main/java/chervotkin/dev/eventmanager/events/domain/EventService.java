package chervotkin.dev.eventmanager.events.domain;

import chervotkin.dev.eventmanager.events.api.*;
import chervotkin.dev.eventmanager.events.db.EventEntity;
import chervotkin.dev.eventmanager.events.db.EventRegistrationEntity;
import chervotkin.dev.eventmanager.events.db.EventRepository;
import chervotkin.dev.eventmanager.kafka.EventChangeProducer;
import chervotkin.dev.eventmanager.kafka.dto.*;
import chervotkin.dev.eventmanager.locations.LocationService;
import chervotkin.dev.eventmanager.users.domain.AuthenticationService;
import chervotkin.dev.eventmanager.users.domain.UserRole;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventService {

    private final static Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    private final LocationService locationService;

    private final AuthenticationService authenticationService;

    private final EventEntityMapper entityMapper;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final EventChangeProducer eventChangeProducer;

    public EventService(EventRepository eventRepository, LocationService locationService, AuthenticationService authenticationService, EventEntityMapper entityMapper, ApplicationEventPublisher applicationEventPublisher, EventChangeProducer eventChangeProducer) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.authenticationService = authenticationService;
        this.entityMapper = entityMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.eventChangeProducer = eventChangeProducer;
    }

    public Event createEvent(EventCreateRequestDto createRequest) {

        var location = locationService.getLocationById(createRequest.locationId());
        if (location.capacity() < createRequest.maxPlaces()) {
            throw new IllegalArgumentException("Capacity of location is: %s, but maxPlaces is: %s"
                    .formatted(location.capacity(), createRequest.maxPlaces()));
        }

        var currentUser = authenticationService.getCurrentAuthenticatedUser();

        var entity = new EventEntity(
                null,
                createRequest.name(),
                currentUser.id(),
                createRequest.maxPlaces(),
                List.of(),
                createRequest.date(),
                createRequest.cost(),
                createRequest.duration(),
                createRequest.locationId(),
                EventStatus.WAIT_START
        );

        entity = eventRepository.save(entity);

        log.info("New event was created: eventID={}", entity.getId());

        return entityMapper.toDomain(entity);
    }

    public Event getEventById(Long eventId) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event entity wasn't found by id=%s"
                        .formatted(eventId)));
        return entityMapper.toDomain(event);
    }

    @Transactional
    public void cancelEvent(Long eventId) {
        checkCurrentUserCanModifyEvent(eventId);
        var event = getEventById(eventId);

        if (event.status().equals(EventStatus.CANCELLED)) {
            log.info("Event was already cancelled");
            return;
        }
        if (event.status().equals(EventStatus.FINISHED) || event.status().equals(EventStatus.STARTED)) {
            throw new IllegalArgumentException("Cannot cancel event with status=%s".formatted(event.status()));
        }

        eventRepository.changeEventStatus(eventId, EventStatus.CANCELLED);
    }

    @Transactional
    public Event updateEvent(Long eventId,
                             @Valid EventUpdateRequestDto updateRequest) {

        checkCurrentUserCanModifyEvent(eventId);

        var event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Event not found by id=" + eventId)
                );

        if (!event.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException(
                    "Cannot modify event in status: " + event.getStatus());
        }

        var oldName = event.getName();
        var oldMaxPlaces = event.getMaxPlaces();
        var oldDate = event.getDate();
        var oldCost = event.getCost();
        var oldDuration = event.getDuration();
        var oldLocationId = event.getLocationId();
        var oldStatus = event.getStatus();


        if (updateRequest.maxPlaces() != null || updateRequest.locationId() != null) {

            var newLocationId = Optional.ofNullable(updateRequest.locationId())
                    .orElse(oldLocationId);

            var newMaxPlaces = Optional.ofNullable(updateRequest.maxPlaces())
                    .orElse(oldMaxPlaces);

            var location = locationService.getLocationById(newLocationId);

            if (location.capacity() < newMaxPlaces) {
                throw new IllegalArgumentException(
                        "Capacity of location less than maxPlaces: capacity="
                                + location.capacity() + ", maxPlaces=" + newMaxPlaces
                );
            }
        }

        if (updateRequest.maxPlaces() != null &&
                event.getRegistrationList().size() > updateRequest.maxPlaces()) {

            throw new IllegalArgumentException(
                    "Registration count is more than maxPlaces: regCount="
                            + event.getRegistrationList().size()
                            + ", maxPlaces=" + updateRequest.maxPlaces()
            );
        }


        Optional.ofNullable(updateRequest.name()).ifPresent(event::setName);
        Optional.ofNullable(updateRequest.maxPlaces()).ifPresent(event::setMaxPlaces);
        Optional.ofNullable(updateRequest.date()).ifPresent(event::setDate);
        Optional.ofNullable(updateRequest.cost()).ifPresent(event::setCost);
        Optional.ofNullable(updateRequest.duration()).ifPresent(event::setDuration);
        Optional.ofNullable(updateRequest.locationId()).ifPresent(event::setLocationId);

        eventRepository.save(event);

        EventChangeKafkaMessage msg = new EventChangeKafkaMessage();
        msg.setEventId(event.getId());
        msg.setOwnerId(event.getOwnerId());
        msg.setChangedById(authenticationService.getCurrentAuthenticatedUser().id());
        msg.setUsers(event.getRegistrationList()
                .stream()
                .map(EventRegistrationEntity::getUserId)
                .toList()
        );

        boolean changed = false;

        if (!Objects.equals(oldName, event.getName())) {
            msg.setName(new FieldChangeString(oldName, event.getName()));
            changed = true;
        }

        if (!Objects.equals(oldMaxPlaces, event.getMaxPlaces())) {
            msg.setMaxPlaces(new FieldChangeInteger(oldMaxPlaces, event.getMaxPlaces()));
            changed = true;
        }

        if (!Objects.equals(oldDate, event.getDate())) {
            msg.setDate(new FieldChangeDateTime(oldDate, event.getDate()));
            changed = true;
        }

        if (!Objects.equals(oldCost, event.getCost())) {
            msg.setCost(new FieldChangeInteger(oldCost, event.getCost()));
            changed = true;
        }

        if (!Objects.equals(oldDuration, event.getDuration())) {
            msg.setDuration(new FieldChangeInteger(oldDuration, event.getDuration()));
            changed = true;
        }

        if (!Objects.equals(oldLocationId, event.getLocationId())) {
            msg.setLocationId(new FieldChangeLong(oldLocationId, event.getLocationId()));
            changed = true;
        }

        if (!Objects.equals(oldStatus, event.getStatus())) {
            msg.setStatus(new FieldChangeStatus(oldStatus, event.getStatus()));
            changed = true;
        }

        if (changed) {
            eventChangeProducer.send(msg);
        }

        return getEventById(eventId);
    }


    private void checkCurrentUserCanModifyEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = getEventById(eventId);

        if (!event.ownerId().equals(currentUser.id()) && !currentUser.role().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("This user cannot modify this event");
        }

    }

    public List<Event> searchByFilter(EventSearchFilter searchFilter) {
        var foundEntities = eventRepository.findEvents(
                searchFilter.name(),
                searchFilter.placesMin(),
                searchFilter.placesMax(),
                searchFilter.dateStartAfter(),
                searchFilter.dateStartBefore(),
                searchFilter.costMin(),
                searchFilter.costMax(),
                searchFilter.durationMin(),
                searchFilter.durationMax(),
                searchFilter.locationId(),
                searchFilter.eventStatus()
        );

        return foundEntities.stream()
                .map(entityMapper::toDomain)
                .toList();

    }

    public List<Event> getCurrentUserEvents() {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var userEvents = eventRepository.findAllByOwnerIdIs(currentUser.id());

        return userEvents.stream()
                .map(entityMapper::toDomain)
                .toList();
    }

}
