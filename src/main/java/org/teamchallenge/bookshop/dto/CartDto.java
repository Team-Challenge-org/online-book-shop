package org.teamchallenge.bookshop.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
public class CartDto {
    private UUID id;
    private Boolean isPermanent;
    private LocalDate lastModified;
    private List<BookDto> items;
}