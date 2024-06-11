package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.enums.Category;
import org.teamchallenge.bookshop.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToString")
    @Mapping(target = "totalQuantity", source = "quantity")
    BookDto entityToDTO(Book book);
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToString")
    BookInCatalogDto entityToBookCatalogDTO(Book book);
    @Mapping(target = "quantity", source = "totalQuantity")
    Book dtoToEntity(BookDto bookDto);

    @Named("categoryToString")
    default String categoryToString(Category category) {
        return category.getName();
    }
}
