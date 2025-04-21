package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequester(User requester);

    List<ParticipationRequest> findAllByEvent(Event event);

    Optional<ParticipationRequest> findByEventAndRequester(Event event, User requester);

//    @Query("""
//            SELECT r
//            FROM Request as r
//            WHERE (r.eventId.id = :eventId)
//            AND (r.id IN :requestIds)
//            """)
//    Collection<ParticipationRequest> findAllRequestsOnEventByIds(long eventId, List<Long> requestIds);

    Optional<ParticipationRequest> findByEventIdAndRequesterIdIn(Long eventId, List<Long> requestsIds);
}