package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.StateActionUser;
import ru.practicum.event.model.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventPublicParam;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.MapperEvent;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.*;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service("eventServiceImpl")
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;//  private final
    private final RestTemplate restTemplate = new RestTemplate();
    private final CategoryService categoryService;

    @PersistenceUnit
    private EntityManagerFactory entityManager;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void initQueryFactory() {
        queryFactory = new JPAQueryFactory(entityManager.createEntityManager());
    }

    @Override
    public EventFullDto getEventById(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " was not found"));
        Location proxyLocation = event.getLocation();
        Location location = new Location(proxyLocation.getId(), proxyLocation.getLat(), proxyLocation.getLon());
        event.setLocation(location);
        return MapperEvent.toEventFullDto(event);
    }

    @Override
    public Collection<EventShortDto> getEventsByFilter(EventPublicParam param) {
        Collection<Event> events = new ArrayList<>();
        QEvent event = QEvent.event;
        BooleanExpression expression = event.state.eq(EventState.PUBLISHED);

        if (Objects.nonNull(param.getRangeStart()) && Objects.nonNull(param.getRangeEnd())) {
            expression = expression.and(event.eventDate.between(param.getRangeStart(), param.getRangeEnd()));
        } else {
            expression = expression.and(event.eventDate.after(LocalDateTime.now()));
        }
        if (Objects.nonNull(param.getText())) {
            expression = expression.and(event.description.containsIgnoreCase(param.getText()))
                    .or(event.annotation.containsIgnoreCase(param.getText()));
        }
        if (Objects.nonNull(param.getPaid())) {
            expression = expression.and(event.paid.eq(param.getPaid()));
        }
        if (Objects.nonNull(param.getCategories())) {
            expression = expression.and(event.category.id.in(param.getCategories()));
        }
        if (Objects.nonNull(param.getOnlyAvailable()) && param.getOnlyAvailable()) {
            expression = expression.and(event.confirmedRequests.lt(event.participantLimit));
        }
        JPAQuery<Event> query = queryFactory.selectFrom(event)
                .where(expression)
                .offset(param.getFrom())
                .limit(param.getSize());

        if (Objects.nonNull(param.getSort())) {
            if (param.getSort().equals("VIEWS")) {
                events = query.orderBy(event.views.asc()).fetch();
            }
            if (param.getSort().equals("EVENT_DATE")) {
                events = query.orderBy(event.eventDate.asc()).fetch();
            }
        } else {
            events = query.fetch();
        }

        return events.stream()
                .map(MapperEvent::toEventShortDto)
                .toList();
    }

    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate() != null && !newEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new EventDateValidationException("Event date 2+ hours after now");
        }
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category was not found " + newEventDto.getCategory()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with was not found " + userId));
        Event event = MapperEvent.toEvent(newEventDto, category, user);
        event.setLocation(locationRepository.save(newEventDto.getLocation()));

        if (newEventDto.getPaid() == null) {
            event.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        return MapperEvent.toEventFullDto(eventRepository.save(event));

    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return eventRepository.findAllByInitiatorId(userId, page).stream()
                .map(MapperEvent::toEventShortDto).toList();
    }


    @Override
    public EventFullDto getEventById(Long userId, Integer eventId) {
        return MapperEvent.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId)));
    }


    @Override
    public EventFullDto updateEvent(Long userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));

        validateForPrivate(event.getState(), updateEventUserRequest.getStateAction());

        Category category = categoryRepository.findById(updateEventUserRequest.getCategory())
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));

        Location location = locationRepository.save(updateEventUserRequest.getLocation());

        if (updateEventUserRequest.getEventDate() != null && !updateEventUserRequest.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new EventDateValidationException("Event date 2+ hours after now");
        }

        MapperEvent.updateFromDto(event, updateEventUserRequest, category, location);
        return MapperEvent.toEventFullDto(eventRepository.save(event));
    }

    private void validateForPrivate(EventState eventState, StateActionUser stateActionUser) {
        if (eventState.equals(EventState.PUBLISHED)) {
            throw new ConflictException("Can't change event not cancelled or in moderation");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> search(EventSearchParameters parameters) {
        BooleanExpression expression = QEvent.event.id.gt(parameters.from());

        if (parameters.usersIds() != null) {
            expression = expression.and(QEvent.event.initiator.id.in(parameters.usersIds()));
        }

        if (parameters.eventStates() != null) {
            expression = expression.and(QEvent.event.state.in(parameters.eventStates()));
        }

        if (parameters.categoriesIds() != null) {
            expression = expression.and(QEvent.event.category.id.in(parameters.categoriesIds()));
        }

        if (parameters.rangeStart() != null && parameters.rangeEnd() != null) {
            if (parameters.rangeStart().isAfter(parameters.rangeEnd())) {
                throw new InvalidDateException("Время начала диапазона поиска позже времени завершения");
            }

            expression = expression.and(QEvent.event.eventDate.between(parameters.rangeStart(), parameters.rangeEnd()));
        } else if (parameters.rangeStart() != null) {
            expression = expression.and(QEvent.event.eventDate.after(parameters.rangeStart()));
        } else if (parameters.rangeEnd() != null) {
            expression = expression.and(QEvent.event.eventDate.before(parameters.rangeEnd()));
        }

        Page<Event> result;
        Pageable pageable = PageRequest.of(0, parameters.size());
        result = eventRepository.findAll(expression, pageable);

        return result.stream()
                .map(MapperEvent::toEventFullDto)
                .toList();
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventDto updateEventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("События с id = " + eventId + " не найдено"));

        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }

        if (updateEventDto.getCategory() != null) {
            Category category = categoryService.findCategoryById(updateEventDto.getCategory());
            event.setCategory(category);
        }

        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }

        if (updateEventDto.getEventDate() != null) {
            event.setEventDate(updateEventDto.getEventDate());
        }

        if (updateEventDto.getLocation() != null) {
            event.setLocation(updateEventDto.getLocation());
        }

        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }

        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }

        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }

        if (updateEventDto.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(updateEventDto.getStateAction());
            if (stateAction == StateAction.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING) {
                    throw new InvalidStateException("Событие можно публиковать, только если оно в состоянии ожидания публикации");
                }

                if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                    throw new InvalidDateException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
                }

                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }

            if (stateAction == StateAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new InvalidStateException("Событие можно отклонить, только если оно еще не опубликовано");
                }

                event.setState(EventState.CANCELED);
            }
        }

        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }

        return MapperEvent.toEventFullDto(eventRepository.save(event));
    }

}
