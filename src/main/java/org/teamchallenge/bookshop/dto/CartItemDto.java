package org.teamchallenge.bookshop.dto;

import org.teamchallenge.bookshop.model.Book;

public record CartItemDto(Book book, int count) {
}
