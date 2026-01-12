package com.metara.metara.repository;
import com.metara.metara.models.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long>{



    List<Event> findByTitle(String title);
    List<Event> findByLocation(String location);

    Optional<Event> findByUserId(Long userId);

    List<Event> findByEventDateAfterAndTitleContainingIgnoreCase(
            LocalDateTime now,
            String keyword
    );
    List<Event> findByEventDateBeforeAndTitleContainingIgnoreCase(
            LocalDateTime now,
            String keyword
    );

    List<Event> findByEventDateAfter(LocalDateTime now);
    List<Event> findByEventDateBefore(LocalDateTime now);
    List<Event> findByTitleContainingIgnoreCase(String keyword);
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDateTime now);
    List<Event> findByEventDateAfterOrderByEventDateDesc(LocalDateTime now);

    List<Event> findByEventDateBeforeOrderByEventDateAsc(LocalDateTime now);
    List<Event> findByEventDateBeforeOrderByEventDateDesc(LocalDateTime now);
    Page<Event> findByEventDateAfter(LocalDateTime now, Pageable pageable);
    Page<Event> findByEventDateBefore(LocalDateTime now, Pageable pageable);
}
