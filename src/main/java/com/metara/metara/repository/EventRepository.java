package com.metara.metara.repository;
import com.metara.metara.models.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long>{

    List<Event> findByEventDateAfterAndTitleContainingIgnoreCase(
            LocalDateTime now,
            String keyword
    );

    Optional<Event> findByTitle(String title);
    Optional<Event> findByLocation(String location);

    Optional<Event> findByUserId(Long userId);

    List<Event> findByEventDateAfter(LocalDateTime now);
    List<Event> findByTitleContainingIgnoreCase(String keyword);
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDateTime now);

    Page<Event> findByEventDateAfter(LocalDateTime now, Pageable pageable);
}
