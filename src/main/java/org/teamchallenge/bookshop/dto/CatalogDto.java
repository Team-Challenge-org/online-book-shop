package org.teamchallenge.bookshop.dto;

import org.teamchallenge.bookshop.enums.Category;

public record CatalogDto(long id,
                         Category name) {
}
