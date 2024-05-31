package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryName", source = "category.name")
    BookDto entityToDTO(Book book);
    @Mapping(target = "categoryName", source = "category.name")
    BookInCatalogDto entityToBookCatalogDTO(Book book);
    Book dtoToEntity(BookDto bookDto);
}