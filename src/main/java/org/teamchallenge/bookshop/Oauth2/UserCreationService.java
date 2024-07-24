package org.teamchallenge.bookshop.Oauth2;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.dto.OAuth2UserInfo;
import org.teamchallenge.bookshop.enums.Role;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserCreationService {
    private final UserRepository userRepository;

    @Transactional
    public User createNewUser(OAuth2UserInfo oauth2UserInfo) {
        User newUser = new User();
        newUser.setName(oauth2UserInfo.getName());
        newUser.setEmail(oauth2UserInfo.getEmail());
        newUser.setProvider(oauth2UserInfo.getProvider());
        newUser.setProviderId(oauth2UserInfo.getProviderId());
        newUser.setRole(Role.USER);
        Cart newCart = new Cart();
        newCart.setIsPermanent(true);
        newCart.setLastModified(LocalDate.now());
        newUser.setCart(newCart);

        return userRepository.save(newUser);
    }
}
