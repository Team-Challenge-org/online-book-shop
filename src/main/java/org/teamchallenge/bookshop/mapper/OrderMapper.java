package org.teamchallenge.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.teamchallenge.bookshop.dto.OrderDto;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Order;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {

    @Mapping(target = "books", source = "books", qualifiedByName = "mapBooksToBookIds")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "total", source = "total")
    OrderDto toOrderDto(Order order);
    @Named("mapBooksToBookIds")
    default Map<Long, Integer> mapBooksToBookIds(Map<Book, Integer> books) {
        return books.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getId(), Map.Entry::getValue));
    }
}
