package com.metara.metara.repository;

import com.metara.metara.models.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    List<Role> findAllByOrderByNameAsc();

    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId")
    Set<Role> findRolesByUserId(@Param("userId") Long userId);



}
