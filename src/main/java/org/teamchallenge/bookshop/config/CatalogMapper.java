package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.teamchallenge.bookshop.dto.CatalogDto;
import org.teamchallenge.bookshop.model.Catalog;
@Mapper(config = MapperConfig.class)
public interface CatalogMapper {
    CatalogDto entityToDTO(Catalog catalog);
}
