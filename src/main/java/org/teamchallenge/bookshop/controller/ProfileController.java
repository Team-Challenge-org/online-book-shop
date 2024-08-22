package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.ProfileUpdateDto;
import org.teamchallenge.bookshop.dto.UserDto;
import org.teamchallenge.bookshop.service.ProfileService;

@RestController
@RequestMapping("api/v1/profile")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Profile controller", description = "API for profile management")
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "Update user profile", description = "Allows users to update their profile information such as firstName,lastName, email, phone number, password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileUpdateDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PatchMapping("/update")
    public ProfileUpdateDto updateProfile(@RequestBody ProfileUpdateDto profileUpdateDto) {
        return profileService.updateProfile(profileUpdateDto);
    }
    @GetMapping("/user")
    public ResponseEntity<ProfileUpdateDto> getUserData(){
        ProfileUpdateDto profileUpdateDto = profileService.getUserData();
        return ResponseEntity.ok(profileUpdateDto);
    }

    @PatchMapping("/reset-password")
    public ProfileUpdateDto resetPasswordProfile(@RequestBody String password) {
        return profileService.resetPassword(password);
    }
}
