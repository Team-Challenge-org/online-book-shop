package org.teamchallenge.bookshop.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.dto.SliderDto;
import org.teamchallenge.bookshop.model.Slider;
import org.teamchallenge.bookshop.repository.SliderRepository;
import org.teamchallenge.bookshop.service.SliderService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SliderServiceImpl implements SliderService {
    private final SliderRepository sliderRepository;

    @Override
    public List<SliderDto> getBooksForSlider() {
        List<Slider> sliders = sliderRepository.findAll();
        return sliders.stream()
                .map(Slider::convertToDto)
                .collect(Collectors.toList());

    }
}