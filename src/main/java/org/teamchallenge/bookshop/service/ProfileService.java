package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.dto.ProfileUpdateDto;
import org.teamchallenge.bookshop.dto.UserDto;

public interface ProfileService {
    ProfileUpdateDto updateProfile(ProfileUpdateDto profileDto);

    ProfileUpdateDto getUserData();

    ProfileUpdateDto resetPassword(String password);
}
