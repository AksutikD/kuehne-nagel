package com.logistic.kuehnenagel.rest;

import com.logistic.kuehnenagel.converters.CityConverter;
import com.logistic.kuehnenagel.dto.CityGetDto;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.dto.CitySearchDto;
import com.logistic.kuehnenagel.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/cities")
@RequiredArgsConstructor
@CrossOrigin
public class CityController {

    private final CityService cityService;

    @GetMapping
    public Page<CityGetDto> getAll(@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                   @RequestParam(required = false) String name) {
        return cityService.getAll(new CitySearchDto(name), pageable).map(CityConverter::cityToGetDtoConvert);
    }

    @PutMapping
    public CityGetDto updateCity(@RequestBody @Valid CityPostDto cityPostDto) {
        return CityConverter.cityToGetDtoConvert(cityService.updateCity(cityPostDto));
    }
}
