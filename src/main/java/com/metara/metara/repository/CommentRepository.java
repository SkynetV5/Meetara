package com.metara.metara.repository;


import com.metara.metara.models.entity.Comment;
import com.metara.metara.models.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByCreatedAtBeforeOrderByTextAsc(LocalDateTime now);
    List<Comment> findByCreatedAtBeforeOrderByTextDesc(LocalDateTime now);
    List<Comment> findByCreatedAtAfterOrderByTextAsc(LocalDateTime now);
    List<Comment> findByCreatedAtAfterOrderByTextDesc(LocalDateTime now);

    List<Comment> findAllByEventId(Long eventId);
    List<Comment> findAllByUserId(Long userId);

    List<Comment> findAllByCreatedAt(LocalDateTime date);

}
