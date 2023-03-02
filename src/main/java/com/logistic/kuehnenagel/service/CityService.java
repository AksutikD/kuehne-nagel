package com.logistic.kuehnenagel.service;

import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    @Transactional(readOnly = true)
    public City getById(final Long id) {
        return cityRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("City with '" + id + "' id not found."));
    }

    @Transactional
    public void createCities(final List<City> cityList) {
        cityRepository.saveAll(cityList);
    }

    @Transactional(readOnly = true)
    public Page<City> getAll(final String name, final Pageable pageable) {
        if (StringUtils.isBlank(name)) {
            return cityRepository.findAll(pageable);
        } else {
            return cityRepository.findAllByName(name, pageable);
        }
    }

    @Transactional
    public City uploadCity(final CityPostDto cityPostDto) {
        City city = getById(cityPostDto.getId());

        city.setName(cityPostDto.getName());
        city.setImageUrl(cityPostDto.getImageUrl());

        return city;
    }
}
