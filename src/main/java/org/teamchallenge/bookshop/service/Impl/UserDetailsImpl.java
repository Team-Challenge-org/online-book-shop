package org.teamchallenge.bookshop.service.Impl;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
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

    private UserRepository userRepository;



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)   {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
