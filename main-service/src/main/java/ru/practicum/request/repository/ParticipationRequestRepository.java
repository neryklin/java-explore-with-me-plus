package ru.practicum.request.repository;

import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.user.model.User;
import ru.practicum.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequester(User requester);

    List<ParticipationRequest> findAllByEvent(Event event);

    Optional<ParticipationRequest> findByEventAndRequester(Event event, User requester);
}