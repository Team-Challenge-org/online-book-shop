package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.teamchallenge.bookshop.dto.AuthorDto;
import org.teamchallenge.bookshop.model.Author;

@Mapper(config = MapperConfig.class)
public interface AuthorMapper {
    AuthorDto entityToDTO(Author author);
    Author dtoToEntity(AuthorDto authorDto);
}
