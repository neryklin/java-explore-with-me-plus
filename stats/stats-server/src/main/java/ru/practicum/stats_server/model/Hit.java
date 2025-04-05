package ru.practicum.stats_server.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.DATE;

@Entity
@Table(name = "hits")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String app;

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false)
    private String ip;

    @DateTimeFormat(pattern = DATE)
    @Column(name = "created", nullable = false)
    private LocalDateTime timestamp;
}
