package com.metara.metara.repository;


import com.metara.metara.models.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation,Long> {

    Optional<Participation> findByUserId(Long userId);
    List<Participation> findByEventId(Long eventId);

    Optional<Participation> findByStatus(Participation.Status status);

    Optional<Participation> findByRegisteredAt(LocalDateTime date);

    List<Participation> findByRegisteredAtAfter(LocalDateTime date);

    List<Participation> findByRegisteredAtBefore(LocalDateTime date);
}
