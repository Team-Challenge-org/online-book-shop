package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.dto.CartItemDto;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Cart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface CartMapper {
    @Mappings({
            @Mapping(source = "items", target = "items"),
            @Mapping(source = "total", target = "total")
    })
    CartDto entityToDTO(Cart cart);

    @Mappings({
            @Mapping(source = "items", target = "items"),
            @Mapping(source = "total", target = "total")
    })
    Cart dtoToEntity(CartDto cartDto);

    default List<CartItemDto> map(Map<Book, Integer> items) {
        return items.entrySet().stream()
                .map(entry -> new CartItemDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    default Map<Book, Integer> map(List<CartItemDto> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        CartItemDto::book,
                        CartItemDto::count
                ));
    }
}
