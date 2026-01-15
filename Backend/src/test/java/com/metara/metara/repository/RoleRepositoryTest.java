package com.metara.metara.repository;

import com.metara.metara.models.entity.Role;
import com.metara.metara.models.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Role roleAdmin;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        // Tworzymy role
        roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");

        roleUser = new Role();
        roleUser.setName("ROLE_USER");

        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);


        user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setEnabled(true);
        user.setRoles(Set.of(roleAdmin, roleUser));

        userRepository.save(user);
    }

    @Test
    void testFindRolesByUserId() {
        Set<Role> roles = roleRepository.findRolesByUserId(user.getId());

        assertThat(roles).isNotNull();
        assertThat(roles).hasSize(2);
        assertThat(roles).extracting("name")
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }
}

