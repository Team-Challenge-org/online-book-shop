package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.model.Book;

@Mapper
public interface BookMapper {
    BookMapper BOOK_MAPPER = Mappers.getMapper(BookMapper.class);
    BookDto entityToDTO(Book book);
    BookInCatalogDto entityToCatalogDTO(Book book);
    Book dtoToEntity(BookDto bookDto);
}
