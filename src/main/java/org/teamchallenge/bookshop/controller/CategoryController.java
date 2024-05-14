package org.teamchallenge.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamchallenge.bookshop.dto.CatalogDto;
import org.teamchallenge.bookshop.service.CatalogService;

import java.util.List;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(maxAge = 3600, origins = "*")
public class CategoryController {
    private final CatalogService catalogService;

    @GetMapping("/all")
    public ResponseEntity<List<CatalogDto>> getAllCategory() {
        return ResponseEntity.ok(catalogService.getAllCategory());
    }
}
