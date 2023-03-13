package com.logistic.kuehnenagel.service;

import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.dto.CitySearchDto;
import com.logistic.kuehnenagel.error.ServiceError;
import com.logistic.kuehnenagel.error.ServiceException;
import com.logistic.kuehnenagel.repository.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CityServiceTest {

    @Autowired
    private com.logistic.kuehnenagel.service.CityService cityService;

    @MockBean
    private CityRepository cityRepository;

    @Test
    public void getByIdSuccess() {
        City city = new City(1L, "Tokyo", "http://testurl");

        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));

        City foundCity = cityService.getById(1L);

        assertThat(foundCity).isNotNull();
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    public void getByIdThrowsExceptionSuccess() {
        when(cityRepository.findById(1L)).thenThrow(ServiceException
                .builder(ServiceError.MISSING_OBJECT_BY_ID)
                .messageParameters("City", 1L)
                .build());

        Assertions.assertThrows(ServiceException.class, () -> cityService.getById(1L));
    }

    @Test
    public void createThreeCitiesSuccess() {
        List<City> cities = List.of(
                new City(1L, "Tokyo", "http://testurl"),
                new City(2L, "Singapore", "http://testurl"),
                new City(3L, "Tokyo", "http://testurl"));

        cityService.createCities(cities);

        verify(cityRepository, times(1)).saveAll(cities);
    }

    @Test
    public void getAllCitiesSuccess() {
        cityService.getAll(new CitySearchDto("Tokyo"), PageRequest.of(0, 10));

        verify(cityRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void updateCitySuccess() {
        CityPostDto cityPostDto = new CityPostDto(1L, "Tokyo", "http://testurl");
        City city = new City(1L, "Abu Dhabi", "http://changedUrl");

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        City updatedCity = cityService.updateCity(cityPostDto);

        assertThat(updatedCity).isNotNull();
        verify(cityRepository, times(1)).findById(1L);
    }
}
