package org.teamchallenge.bookshop.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.UserRepository;

import java.util.Collections;
@Service
@AllArgsConstructor
public class UserDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailOrPhone) {
        User user = userRepository.findByEmailOrPhoneNumber(emailOrPhone)
                .orElseThrow(UserNotFoundException::new);

        return buildUserDetails(user);
    }
    private UserDetails buildUserDetails(User user) {
        String emailOrPhone = user.getEmail() != null ? user.getEmail() : user.getPhoneNumber();

        return new org.springframework.security.core.userdetails.User(
                emailOrPhone,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }



}
