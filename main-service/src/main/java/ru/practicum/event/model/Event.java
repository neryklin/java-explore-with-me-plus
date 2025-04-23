package ru.practicum.event.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id")
    Long id;

    @Column(name = "annotation")
    String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    // @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    //@ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id")
    Location location;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @Column(name = "paid")
    Boolean paid;

    @Column(name = "request_moderation")
    Boolean requestModeration;

    @Column(name = "participant_limit")
    Long participantLimit;

    @Column(name = "confirmedRequests")
    Long confirmedRequests;

    @Column(name = "state", length = 50)
    @Enumerated(EnumType.STRING)
    EventState state;

    @Column(name = "createdOn")
    LocalDateTime createdOn;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "views")
    Long views;
}