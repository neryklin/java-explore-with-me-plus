package ru.practicum.event.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.util.Collection;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Page<Event> findAllByInitiatorId(Long userId, Pageable page);

    Optional<Event> findByIdAndInitiatorId(Integer eventId, Long userId);

    Optional<Event> findByIdAndState(Long id, EventState state);

    @Query(value = "SELECT e FROM Event AS e " +
            "WHERE distance(:lat, :lon, e.location.lat, e.location.lon) < :maxDistance")
    Collection<Event> findEventsByLocation(float lat, float lon, float maxDistance);

}
