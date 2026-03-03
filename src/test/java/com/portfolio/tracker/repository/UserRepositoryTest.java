package com.portfolio.tracker.repository;

import com.portfolio.tracker.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("johndoe")
                .email("john@example.com")
                .password("encodedPassword")
                .build();
        entityManager.persistAndFlush(user);
    }

    @Test
    void findByUsername_shouldReturnUser_whenUsernameExists() {
        Optional<User> result = userRepository.findByUsername("johndoe");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("johndoe");
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {
        Optional<User> result = userRepository.findByUsername("unknown");

        assertThat(result).isEmpty();
    }
}
