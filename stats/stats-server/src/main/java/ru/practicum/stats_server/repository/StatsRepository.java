package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stats_server.model.Hit;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {

}
