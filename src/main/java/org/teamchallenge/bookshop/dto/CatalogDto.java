package org.teamchallenge.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.teamchallenge.bookshop.enums.Category;

import java.math.BigDecimal;

public record CatalogDto(long id,
                         BigDecimal price,
                         @JsonIgnore Category category,
                         @JsonProperty("categoryName") String categoryName) {

}
