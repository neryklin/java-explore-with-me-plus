package ru.practicum.request.dto;

import lombok.*;
import ru.practicum.request.enums.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
