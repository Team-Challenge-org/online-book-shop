package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto entityToDTO(Book book);
    BookInCatalogDto entityToCatalogDTO(Book book);
    Book dtoToEntity(BookDto bookDto);
}