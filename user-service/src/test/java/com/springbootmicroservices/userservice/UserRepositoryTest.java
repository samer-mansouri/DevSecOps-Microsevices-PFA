package com.springbootmicroservices.userservice;

import com.springbootmicroservices.userservice.entity.User;
import com.springbootmicroservices.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setUsername("samer");
        user.setPassword("secret123");
        user.setName("Samer");
        user.setSurname("Mansouri");
        user.setPhoneNumber("+21612345678");
        user.setEmail("samer@example.com");
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("samer");
        assertThat(found.get().getEmail()).isEqualTo("samer@example.com");
    }
}
