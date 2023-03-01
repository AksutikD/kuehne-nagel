package com.logistic.kuehnenagel.service;

import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.error.ResourceNotFoundException;
import com.logistic.kuehnenagel.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    @Transactional(readOnly = true)
    public City getByName(final String name) {
        return cityRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("City with '" + name + "' name not found."));
    }

    @Transactional
    public void createCities(final List<City> cityList) {
        cityRepository.saveAll(cityList);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(final String name) {
        return cityRepository.existsByName(name);
    }

    @Transactional(readOnly = true)
    public Page<City> getAll(final Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Transactional
    public City uploadCity(final CityPostDto cityPostDto) throws IOException {
        City city = getByName(cityPostDto.getOldName());

        city.setName(cityPostDto.getNewName());
        city.setImage(cityPostDto.getMultipartFile().getBytes());

        return city;
    }
}
