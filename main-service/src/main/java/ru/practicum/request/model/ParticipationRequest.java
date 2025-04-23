package ru.practicum.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
}
