package org.teamchallenge.bookshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class AuthControllerTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        Optional<User> existingUser = userRepository.findByEmail("newUser@gmail.com");
        if (existingUser.isEmpty()) {
            User testUser = new User();
            testUser.setPhoneNumber("123456");
            testUser.setEmail("newUser@gmail.com");
            testUser.setName("Misha");
            userRepository.save(testUser);
        }
    }
    @Test
    public void testFindByEmailOrPhoneNumber() {
        String emailOrPhone = "newUser@gmail.com";

        Optional<User> user = userRepository.findByEmailOrPhoneNumber(emailOrPhone);
        assertTrue(user.isPresent(), emailOrPhone);
    }

    @Test
    public void testFindByPhoneNumber() {
        String phoneNumber = "123456";
        Optional<User> user = userRepository.findByEmailOrPhoneNumber(phoneNumber);
        assertTrue(user.isPresent(), phoneNumber);
    }

}