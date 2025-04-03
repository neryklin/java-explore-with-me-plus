package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats_server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT h.app, h.uri, COUNT(h.ip) AS hits " +
            "FROM Hit h " +
            "WHERE (:uris IS NULL OR h.uri IN (:uris)) " +
            "AND (h.timestamp BETWEEN :start AND :end)" +
            "GROUP BY h.app, h.uri " +
            "ORDER BY hits DESC ")
    List<Object[]> findAllHit(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT h.app, h.uri, COUNT(DISTINCT h.ip) as hits " +
            "FROM Hit h " +
            "WHERE (:uris IS NULL OR h.uri IN (:uris)) " +
            "AND (h.timestamp BETWEEN :start AND :end) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY hits DESC")
    List<Object[]> findUniqueHit(LocalDateTime start, LocalDateTime end, List<String> uris);
}
