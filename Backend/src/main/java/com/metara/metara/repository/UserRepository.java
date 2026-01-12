package com.metara.metara.repository;


import com.metara.metara.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<User> findByCreatedAtAfter(LocalDateTime date);

    List<User> findAllByOrderByUsernameAsc();

    Page<User> findByEnabled(boolean enabled, Pageable pageable);



}
