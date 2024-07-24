package org.teamchallenge.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Cart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface CartMapper {

    @Mapping(target = "items", source = "items", qualifiedByName = "mapToList")
    CartDto entityToDto(Cart cart);

    @Mapping(target = "items", source = "items", qualifiedByName = "listToMap")
    Cart dtoToEntity(CartDto cartDto);

    @Named("mapToList")
    default List<BookDto> mapToList(Map<Book, Integer> map) {
        return map.entrySet()
                .stream()
                .map(entry -> {
                    BookDto bookDto = bookToBookDto(entry.getKey());
                    bookDto.setQuantity(entry.getValue());
                    return bookDto;
                })
                .collect(Collectors.toList());
    }

    @Named("listToMap")
    default Map<Book, Integer> listToMap(List<BookDto> list) {
        return list.stream()
                .collect(Collectors.toMap(
                        this::bookDtoToBook,
                        BookDto::getQuantity
                ));
    }

    BookDto bookToBookDto(Book book);
    Book bookDtoToBook(BookDto bookDto);
}