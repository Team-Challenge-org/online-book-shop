package org.teamchallenge.bookshop.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.CatalogMapper;
import org.teamchallenge.bookshop.dto.CatalogDto;
import org.teamchallenge.bookshop.repository.CatalogRepository;
import org.teamchallenge.bookshop.service.CatalogService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository catalogRepository;
    private final CatalogMapper catalogMapper;

    @Override
    public List<CatalogDto> getAllCategory() {
        return catalogRepository.findAll()
                .stream()
                .map(catalogMapper::entityToDTO)
                .collect(Collectors.toList());
    }
}
