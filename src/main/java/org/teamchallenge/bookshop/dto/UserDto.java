package org.teamchallenge.bookshop.dto;

import java.util.List;

public record UserDto(
        long id,
        String name,
        String surname,
        String email,
        String proneNumber,
        String role,
        List<Long> favouriteBookIds,
        long cartId,
        List<Long> orderIds) {
}
