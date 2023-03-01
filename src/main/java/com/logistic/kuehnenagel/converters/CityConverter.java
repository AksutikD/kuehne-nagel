package com.logistic.kuehnenagel.converters;

import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.domain.csv.CsvCity;
import com.logistic.kuehnenagel.dto.CityGetDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CityConverter {

    public static CityGetDto cityToGetDtoConvert(final City city) {
        return new CityGetDto(city.getId(), city.getName(), city.getImageUrl());
    }

    public static City csvCityToCityConvert(final CsvCity csvCity) {
        return new City(csvCity.getId(), csvCity.getName(), csvCity.getImageUrl());
    }
}
