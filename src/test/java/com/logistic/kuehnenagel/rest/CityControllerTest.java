package com.logistic.kuehnenagel.rest;

import com.logistic.kuehnenagel.dto.CityGetDTO;
import com.logistic.kuehnenagel.dto.CityPostDto;
import com.logistic.kuehnenagel.error.ApiErrorResponse;
import com.logistic.kuehnenagel.util.RestResponsePage;
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
public class CityControllerTest {
    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getCityByNameTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<CityGetDTO> responseEntity = restTemplate.exchange( "http://localhost:" + port + "/api/v1/cities/Tokyo",
                HttpMethod.GET,
                entity,
                CityGetDTO.class);

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        CityGetDTO cityGetDTO = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(1, cityGetDTO.getId());
        assertEquals("Tokyo", cityGetDTO.getName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg",
                cityGetDTO.getImageUrl());

        entity = new HttpEntity<>(headers);
        ResponseEntity<ApiErrorResponse> responseEntityError = restTemplate.exchange( "http://localhost:" + port + "/api/v1/cities/asd",
                HttpMethod.GET,
                entity,
                ApiErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntityError.getStatusCode().value());

        ApiErrorResponse apiErrorResponse = responseEntityError.getBody();
        assertNotNull(apiErrorResponse);
        assertEquals("City with 'asd' name not found.", apiErrorResponse.getMessage());
    }

    @Test
    public void uploadCityTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        CityPostDto cityPostDto = new CityPostDto(2L, "Jakarta2", "http://test.com/1.png");
        HttpEntity<CityPostDto> entity = new HttpEntity<>(cityPostDto, headers);
        ResponseEntity<CityGetDTO> responseEntity = restTemplate.exchange( "http://localhost:" + port + "/api/v1/cities/",
                HttpMethod.POST,
                entity,
                CityGetDTO.class);

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        CityGetDTO cityGetDTO = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(2L, cityGetDTO.getId());
        assertEquals("Jakarta2", cityGetDTO.getName());
        assertEquals("http://test.com/1.png", cityGetDTO.getImageUrl());

        cityPostDto = new CityPostDto(null, "Jakarta2", "http://test.com/1.png");
        entity = new HttpEntity<>(cityPostDto, headers);
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
    public void getAllTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<RestResponsePage<CityGetDTO>> responseType = new ParameterizedTypeReference<>() { };

        ResponseEntity<RestResponsePage<CityGetDTO>> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/v1/cities?size=10&page0",
                HttpMethod.GET,
                entity,
                responseType);

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        RestResponsePage<CityGetDTO> page = responseEntity.getBody();
        assertNotNull(page);
        assertEquals(96, page.getTotalPages());
        assertEquals(10, page.getContent().size());
    }
}
