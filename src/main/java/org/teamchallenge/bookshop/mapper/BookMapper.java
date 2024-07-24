package org.teamchallenge.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.enums.Category;
import org.teamchallenge.bookshop.model.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToString")
    BookDto entityToDTO(Book book);
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToString")
    @Mapping(target = "quantity", ignore = true)
    BookInCatalogDto entityToBookCatalogDTO(Book book);
    Book dtoToEntity(BookDto bookDto);

    @Named("categoryToString")
    default String categoryToString(Category category) {
        return category.getName();
    }
}
