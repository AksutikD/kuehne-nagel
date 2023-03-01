package com.logistic.kuehnenagel.converters;

import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.domain.csv.CsvCity;
import com.logistic.kuehnenagel.dto.CityGetDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CityConverter {

    public static CityGetDTO cityToGetDtoConvert(final City city) {
        return new CityGetDTO(city.getId(), city.getName(), city.getImageUrl());
    }

    public static City csvCityToCityConvert(final CsvCity csvCity) {
        return new City(csvCity.getId(), csvCity.getName(), csvCity.getImageUrl());
    }
}
