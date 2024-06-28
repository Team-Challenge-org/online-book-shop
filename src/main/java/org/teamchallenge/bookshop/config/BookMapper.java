package org.teamchallenge.bookshop.config;

import org.mapstruct.*;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.enums.Category;
import org.teamchallenge.bookshop.model.Book;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToString")
    BookDto entityToDTO(Book book);
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToString")
    @Mapping(target = "quantity", ignore = true)
    BookInCatalogDto entityToBookCatalogDTO(Book book);
    Book dtoToEntity(BookDto bookDto);
    @Mapping(target = "id", ignore = true)
    void updateBookFromDto(BookDto dto, @MappingTarget Book entity);

    @Named("categoryToString")
    default String categoryToString(Category category) {
        return category.getName();
    }
}
