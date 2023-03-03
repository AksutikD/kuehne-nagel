package com.logistic.kuehnenagel.test.rest;

import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.dto.CityGetDto;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.error.ApiErrorResponse;
import com.logistic.kuehnenagel.service.CityService;
import com.logistic.kuehnenagel.test.util.RestResponsePage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CityController {
    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CityService cityService;

    @Test
    public void updateCitySuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        CityPostDto cityPostDto = new CityPostDto(2L, "Jakarta2", "http://test.com/1.png");
        HttpEntity<CityPostDto> entity = new HttpEntity<>(cityPostDto, headers);
        ResponseEntity<CityGetDto> responseEntity = restTemplate.exchange( "http://localhost:" + port + "/api/v1/cities/",
                HttpMethod.POST,
                entity,
                CityGetDto.class);

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        City city = cityService.getById(2L);

        assertNotNull(city);
        assertEquals(city.getId(), cityPostDto.getId());
        assertEquals(city.getName(), cityPostDto.getName());
        assertEquals(city.getImageUrl(), cityPostDto.getImageUrl());

        CityGetDto cityGetDTO = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(2L, cityGetDTO.getId());
        assertEquals("Jakarta2", cityGetDTO.getName());
        assertEquals("http://test.com/1.png", cityGetDTO.getImageUrl());
    }

    @Test
    public void updateCityFailure() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        CityPostDto cityPostDto = new CityPostDto(null, "Jakarta2", "http://test.com/1.png");
        HttpEntity<CityPostDto> entity = new HttpEntity<>(cityPostDto, headers);
        ResponseEntity<ApiErrorResponse> responseEntityError = restTemplate.exchange( "http://localhost:" + port + "/api/v1/cities/",
                HttpMethod.POST,
                entity,
                ApiErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntityError.getStatusCode().value());

        ApiErrorResponse apiErrorResponse = responseEntityError.getBody();
        assertNotNull(apiErrorResponse);
        assertEquals("id must not be null", apiErrorResponse.getMessage());
    }

    @Test
    public void getAllCitiesByDefaultSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<RestResponsePage<CityGetDto>> responseType = new ParameterizedTypeReference<>() { };

        ResponseEntity<RestResponsePage<CityGetDto>> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/v1/cities",
                HttpMethod.GET,
                entity,
                responseType);

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        RestResponsePage<CityGetDto> page = responseEntity.getBody();
        assertNotNull(page);
        assertEquals(100, page.getTotalPages());
        assertEquals(10, page.getContent().size());
    }

    @Test
    public void getAllCitiesByNameSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<RestResponsePage<CityGetDto>> responseType = new ParameterizedTypeReference<>() { };

        ResponseEntity<RestResponsePage<CityGetDto>> responseEntity = restTemplate.exchange( "http://localhost:" + port + "/api/v1/cities?name=Changsha",
                HttpMethod.GET,
                entity,
                responseType);

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        RestResponsePage<CityGetDto> page = responseEntity.getBody();

        assertNotNull(page);
        assertEquals(1, page.getTotalPages());
        assertEquals(2, page.getContent().size());
    }

    @Test
    public void getAllCitiesByNotExistedNameSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<RestResponsePage<CityGetDto>> responseType = new ParameterizedTypeReference<>() { };

        ResponseEntity<RestResponsePage<CityGetDto>> responseEntity = restTemplate.exchange( "http://localhost:" + port + "/api/v1/cities?name=asdasd",
                HttpMethod.GET,
                entity,
                responseType);

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        RestResponsePage<CityGetDto> page = responseEntity.getBody();
        assertNotNull(page);
        assertEquals(0, page.getTotalPages());
        assertEquals(0, page.getContent().size());
    }
}
