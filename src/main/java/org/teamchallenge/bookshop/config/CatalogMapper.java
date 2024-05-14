package org.teamchallenge.bookshop.config;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.teamchallenge.bookshop.dto.CatalogDto;
import org.teamchallenge.bookshop.model.Catalog;
@Mapper
public interface CatalogMapper {
    CatalogMapper CATALOG_MAPPER = Mappers.getMapper(CatalogMapper.class);
    CatalogDto entityToDTO(Catalog catalog);
}
