package org.teamchallenge.bookshop.dto;

import jakarta.validation.constraints.NotNull;
import org.teamchallenge.bookshop.enums.Role;

import java.util.List;
public record UserDto(
        @NotNull() long id,
        String name,
        String surname,
        String email,
        String phoneNumber,
        String provider,
        String providerId,
        Role role,
        List<Long> favouriteBookIds,
        long cartId,
        List<Long> orderIds) {
}
