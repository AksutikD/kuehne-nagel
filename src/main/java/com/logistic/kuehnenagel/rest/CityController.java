package com.logistic.kuehnenagel.rest;

import com.logistic.kuehnenagel.converters.CityConverter;
import com.logistic.kuehnenagel.dto.CityGetDTO;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.service.CityService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/cities")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class CityController {

    private final CityService cityService;

    @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CityGetDTO getCityByName(@PathVariable final String name) {
        return CityConverter.cityToGetDtoConvert(cityService.getByName(name));
    }

    @GetMapping
    public Page<CityGetDTO> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                   @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size) {
        return cityService.getAll(PageRequest.of(page, size)).map(CityConverter::cityToGetDtoConvert);
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CityGetDTO uploadCity(CityPostDto cityPostDto) throws IOException {
        return CityConverter.cityToGetDtoConvert(cityService.uploadCity(cityPostDto));
    }
}
