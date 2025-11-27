package com.metara.metara.repository;


import com.metara.metara.models.entity.Comment;
import com.metara.metara.models.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findByCreatedAt(LocalDateTime date);

    Optional<Comment> findByUserId(Long userId);
    Optional<Comment> findByEventId(Long eventId);

    List<Comment> findByCreatedAtBeforeOrderByCreatedAtAsc(LocalDateTime now);
    List<Comment> findByCreatedAtBeforeOrderByCreatedAtDesc(LocalDateTime now);
    List<Comment> findByCreatedAtAfterOrderByCreatedAtAsc(LocalDateTime now);
    List<Comment> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime now);

    List<Comment> findAllByEventId(Long eventId);
    List<Comment> findAllByUserId(Long userId);

}
