package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.dto.ProfileUpdateDto;
import org.teamchallenge.bookshop.mapper.ProfileMapper;
import org.teamchallenge.bookshop.mapper.UserMapper;
import org.teamchallenge.bookshop.model.Profile;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.ProfileRepository;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.service.ProfileService;
import org.teamchallenge.bookshop.service.UserService;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ProfileUpdateDto updateProfile(ProfileUpdateDto profileUpdateDto) {
        User user = userService.getAuthenticatedUser();
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }
        profileMapper.updateProfileFromDto(profileUpdateDto, profile);
        userMapper.updateUserFromProfileDto(profileUpdateDto, user);

        Profile savedProfile = profileRepository.save(profile);
        userRepository.save(user);

        return profileMapper.toProfileUpdateDto(savedProfile);
    }

    @Override
    public ProfileUpdateDto getUserData() {
        User user = userService.getAuthenticatedUser();
        return profileMapper.toProfileUpdateDto(user);
    }

    @Override
    public ProfileUpdateDto resetPassword(String password) {
        ProfileUpdateDto profile = getUserData();
        profile.setPassword(password);
        return profile;
    }
}
