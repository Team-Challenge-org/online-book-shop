package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Image;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "titleImage", ignore = true)
    @Mapping(target = "images", ignore = true)
    BookDto entityToDTO(Book book);
    @Mapping(source = "titleImage", target = "titleImage", qualifiedByName = "imageToString")
    BookInCatalogDto entityToCatalogDTO(Book book);
    @Mapping(target = "titleImage", ignore = true)
    @Mapping(target = "images", ignore = true)
    Book dtoToEntity(BookDto bookDto);

    @Named("imageToString")
    default String imageToString(Image image) {
        return image.getSmallImage();
    }
}