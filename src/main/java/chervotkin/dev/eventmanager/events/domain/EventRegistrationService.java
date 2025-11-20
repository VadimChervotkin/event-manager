package chervotkin.dev.eventmanager.events.domain;

import chervotkin.dev.eventmanager.events.api.EventStatus;
import chervotkin.dev.eventmanager.events.db.EventRegistrationEntity;
import chervotkin.dev.eventmanager.events.db.EventRegistrationRepository;
import chervotkin.dev.eventmanager.events.db.EventRepository;
import chervotkin.dev.eventmanager.users.domain.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository registrationRepository;

    private final EventRepository eventRepository;

    private final EventService eventService;

    private final EventEntityMapper eventEntityMapper;

    public EventRegistrationService(EventRegistrationRepository registrationRepository, EventRepository eventRepository, EventService eventService, EventEntityMapper eventEntityMapper) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.eventEntityMapper = eventEntityMapper;
    }

    @Transactional
    public void registerUserOnEvent(User user, Long eventId) {
        var eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Event entity wasn't found by id=%s".formatted(eventId)
                ));

        if (user.id().equals(eventEntity.getOwnerId())) {
            throw new IllegalArgumentException("Owner cannot register on his event");
        }

        var registration = registrationRepository.findRegistration(user.id(), eventId);
        if (registration.isPresent()) {
            throw new IllegalArgumentException("User already registered on event");
        }

        if (!eventEntity.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException(
                    "Cannot register on event with status=%s".formatted(eventEntity.getStatus())
            );
        }

        var registeredCount = eventEntity.getRegistrationList().size();
        if(registeredCount >= eventEntity.getMaxPlaces()) {
            throw new IllegalArgumentException(
                    "Event if full: registered=%s, maxPlacex=%s"
                            .formatted(registeredCount, eventEntity.getMaxPlaces())
            );
        }

        registrationRepository.save(
                new EventRegistrationEntity(null, user.id(), eventEntity)
        );
    }

    public void cancelUserRegistration(
            User user,
            Long eventId
    ) {
        var event = eventService.getEventById(eventId);

        var registration = registrationRepository.findRegistration(user.id(), eventId)
                .orElseThrow(()-> new IllegalArgumentException("User have not registered on event")
                );

        if (!event.status().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Cannot cancel register on event with status=%s"
                    .formatted(event.status()));
        }

        registrationRepository.delete(registration);
    }


    public List<Event> getUserRegisteredEvents(Long userId) {

        var foundEvents = registrationRepository.findRegisteredEvents(userId);

        return foundEvents.stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }
}
