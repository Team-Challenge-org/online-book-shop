package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamchallenge.bookshop.dto.SliderDto;
import org.teamchallenge.bookshop.service.SliderService;

import java.util.List;

@RestController
@RequestMapping("api/v1/slider")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(maxAge = 3600, origins = "*")
public class SliderController {
    private final SliderService sliderService;

    @Operation(description = "Find all books")
    @GetMapping("/all")
    public ResponseEntity<List<SliderDto>> getAllBooks() {
        return ResponseEntity.ok(sliderService.getBooksForSlider());
    }
}
