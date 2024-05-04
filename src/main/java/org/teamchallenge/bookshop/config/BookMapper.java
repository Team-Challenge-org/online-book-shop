package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.model.Book;


@Mapper
public interface BookMapper {
    BookMapper BOOK_MAPPER = Mappers.getMapper(BookMapper.class);
    @Mapping(target = "timeAdded", ignore = true)
    BookDto bookToBookDto(Book book);
    @Mapping(target = "timeAdded", ignore = true)
    Book bookDtotoBook(BookDto bookDto);

}