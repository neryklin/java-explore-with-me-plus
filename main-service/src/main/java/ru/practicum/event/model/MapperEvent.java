package ru.practicum.event.model;


import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.MapperCategory;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.enums.StateActionUser;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.MapperUser;
import ru.practicum.user.model.User;

import java.time.format.DateTimeFormatter;
import java.util.Objects;


@UtilityClass
public class MapperEvent {

    public static EventFullDto toEventFullDto(Event event) {
        String published = Objects.nonNull(event.getPublishedOn()) ?
                event.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(MapperCategory.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(MapperUser.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(published)
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator) {
        return Event.builder()
                .id(0L)
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(initiator)
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(MapperCategory.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(MapperUser.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static void updateFromDto(
            Event event,
            UpdateEventUserRequest updateEventUserRequest,
            Category category,
            Location location
    ) {
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getStateAction() != null
                && updateEventUserRequest.getStateAction() == StateActionUser.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        }
        if (updateEventUserRequest.getStateAction() != null
                && updateEventUserRequest.getStateAction() == StateActionUser.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (location != null) {
            event.setLocation(location);
        }
    }
}