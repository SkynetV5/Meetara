package com.metara.metara.repository;


import com.metara.metara.models.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findByCreatedAt(LocalDateTime date);

    Optional<Comment> findByUserId(Long userId);
    Optional<Comment> findByEventId(Long eventId);
}
