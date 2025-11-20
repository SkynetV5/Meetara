package com.metara.metara.repository;

import com.metara.metara.models.entity.Profile;
import com.metara.metara.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Optional<Profile> findByUserId(Long userId);

    Optional<Profile> findByFirstName(String firstName);
    Optional<Profile> findByLastName(String lastName);

    boolean existsByUser(User user);
    Optional<Profile> findByPhoneNumber(String phoneNumber);
}
