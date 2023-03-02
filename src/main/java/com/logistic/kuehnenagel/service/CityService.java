package com.logistic.kuehnenagel.service;

import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.dto.CitySearchDto;
import com.logistic.kuehnenagel.error.ServiceError;
import com.logistic.kuehnenagel.error.ServiceException;
import com.logistic.kuehnenagel.repository.CityRepository;
import com.logistic.kuehnenagel.util.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public City getById(final Long id) {
        return cityRepository.findById(id).orElseThrow(() -> {
            throw ServiceException
                    .builder(ServiceError.MISSING_OBJECT_BY_ID)
                    .messageParameters("City", id)
                    .build();
        });
    }

    public void createCities(final List<City> cityList) {
        cityRepository.saveAll(cityList);
    }

    public Page<City> getAll(final CitySearchDto citySearchDto, final Pageable pageable) {
        Specification<City> specification = SearchUtils.createEqualsStatement(citySearchDto::getName, "name");

        return cityRepository.findAll(specification, pageable);
    }

    @Transactional
    public City updateCity(final CityPostDto cityPostDto) {
        City city = getById(cityPostDto.getId());

        city.setName(cityPostDto.getName());
        city.setImageUrl(cityPostDto.getImageUrl());

        return city;
    }
}
