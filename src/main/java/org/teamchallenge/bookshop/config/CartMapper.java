package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Cart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public abstract class CartMapper {

    @Autowired
    protected BookMapper bookMapper;

    @Mapping(target = "items", source = "items", qualifiedByName = "mapToList")
    public abstract CartDto entityToDto(Cart cart);

    @Mapping(target = "items", source = "items", qualifiedByName = "listToMap")
    public abstract Cart dtoToEntity(CartDto cartDto);

    @Named("mapToList")
    public List<BookDto> mapToList(Map<Book, Integer> map) {
        return map.entrySet()
                .stream()
                .map(entry -> {
                    BookDto bookDto = bookMapper.entityToDTO(entry.getKey());
                    bookDto.setQuantity(entry.getValue());
                    return bookDto;
                })
                .collect(Collectors.toList());
    }

    @Named("listToMap")
    public Map<Book, Integer> listToMap(List<BookDto> list) {
        return list.stream()
                .collect(Collectors.toMap(
                        bookDto -> bookMapper.dtoToEntity(bookDto),
                        BookDto::getQuantity
                ));
    }
}