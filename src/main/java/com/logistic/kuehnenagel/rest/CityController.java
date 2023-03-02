package com.logistic.kuehnenagel.rest;

import com.logistic.kuehnenagel.converters.CityConverter;
import com.logistic.kuehnenagel.dto.CityGetDto;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.dto.CitySearchDto;
import com.logistic.kuehnenagel.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CityGetDto> getAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String name) {
        return cityService.getAll(new CitySearchDto(name), PageRequest.of(page, size)).map(CityConverter::cityToGetDtoConvert);
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CityGetDto updateCity(@RequestBody @Valid CityPostDto cityPostDto) {
        return CityConverter.cityToGetDtoConvert(cityService.updateCity(cityPostDto));
    }
}
