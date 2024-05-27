package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.dto.CatalogDto;
import org.teamchallenge.bookshop.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto entityToDTO(Book book);
    CatalogDto entityToCatalogDto(Book book);
    BookInCatalogDto entityToBookCatalogDTO(Book book);
    Book dtoToEntity(BookDto bookDto);
}