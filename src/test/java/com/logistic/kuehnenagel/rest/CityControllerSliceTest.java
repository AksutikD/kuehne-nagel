package com.logistic.kuehnenagel.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.dto.CityGetDto;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.service.CityService;
import com.logistic.kuehnenagel.util.RestResponsePage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityController.class)
public class CityControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @Test
    void getAllTest() throws Exception {
        List<City> cities = List.of(
                new City(1L, "Tokyo", "http://tokyo.com/1.png"),
                new City(5L, "Tokyo", "http://tokyo.com/2.png"),
                new City(6L, "Bilbao", "http://bilbao.com/1.png"),
                new City(2L, "Berlin", "http://berlin.com/1.png"),
                new City(3L, "Ankara", "http://ankara.com/1.png"),
                new City(4L, "Warsaw", "http://warsaw.com/1.png"),
                new City(7L, "Tokyo", "http://tokyo.com/3.png")
        );

        when(cityService.getAll(any(String.class), any(Pageable.class))).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            Pageable pageable = invocation.getArgument(1);
            List<City> filteredByNameCities = cities.stream()
                    .filter(city -> name == null || city.getName().equals(name))
                    .toList();
            List<City> pagedCities = filteredByNameCities.stream()
                    .skip((long) pageable.getPageSize() * pageable.getPageNumber())
                    .limit(pageable.getPageSize())
                    .toList();
            return new PageImpl<>(pagedCities, pageable, filteredByNameCities.size());
        });

        MvcResult result = mockMvc.perform(get("/api/v1/cities?name=Tokyo&size=2&page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        RestResponsePage<CityGetDto> page = new ObjectMapper().readValue(json, RestResponsePage.class);

        assertEquals(2, page.getTotalPages());
        assertEquals(2, page.getContent().size());
        assertEquals(0, page.getNumber());

        result = mockMvc.perform(get("/api/v1/cities?name=Tokyo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        json = result.getResponse().getContentAsString();

        page = new ObjectMapper().readValue(json, RestResponsePage.class);

        assertEquals(1, page.getTotalPages());
        assertEquals(3, page.getContent().size());

        when(cityService.getAll(isNull(), any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(1);
            List<City> pagedCities = cities.stream()
                    .skip((long) pageable.getPageSize() * pageable.getPageNumber())
                    .limit(pageable.getPageSize())
                    .toList();
            return new PageImpl<>(pagedCities, pageable, pagedCities.size());
        });

        result = mockMvc.perform(get("/api/v1/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        json = result.getResponse().getContentAsString();

        page = new ObjectMapper().readValue(json, RestResponsePage.class);

        assertEquals(1, page.getTotalPages());
        assertEquals(7, page.getContent().size());

        verify(cityService, times(3)).getAll(any(), any(Pageable.class));
    }

    @Test
    public void uploadCityTest() throws Exception {
        CityPostDto cityPostDto = new CityPostDto(34L, "Test", "http://test.com/1.png");
        ObjectMapper objectMapper = new ObjectMapper();

        when(cityService.uploadCity(any(CityPostDto.class))).thenAnswer(invocation -> {
            CityPostDto cityPostDto1 = invocation.getArgument(0);

            return new City(cityPostDto1.getId(), cityPostDto1.getName(), cityPostDto1.getImageUrl());
        });

        MvcResult result = mockMvc.perform(post("/api/v1/cities/")
                        .content(objectMapper.writer().writeValueAsString(cityPostDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        CityGetDto cityGetDto = new ObjectMapper().readValue(json, CityGetDto.class);

        assertEquals(cityPostDto.getId(), cityGetDto.getId());
        assertEquals(cityPostDto.getName(), cityGetDto.getName());
        assertEquals(cityPostDto.getImageUrl(), cityGetDto.getImageUrl());

        verify(cityService, times(1)).uploadCity(any(CityPostDto.class));
    }
}
