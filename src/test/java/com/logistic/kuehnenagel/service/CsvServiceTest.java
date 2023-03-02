package com.logistic.kuehnenagel.service;

import com.logistic.kuehnenagel.converters.CityConverter;
import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.domain.csv.CsvCity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CsvServiceTest {

    @Autowired
    private CsvService csvService;

    @Value("classpath:static/cities.csv")
    Resource resourceFile;

    @Test
    public void getStreamFromCSVSuccessTest() throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(resourceFile.getInputStream()))) {
            Stream<List<CsvCity>> csvCityStream = csvService.getStreamFromCSV(reader, CsvCity.class);
            List<List<CsvCity>> csvCityList = csvCityStream.toList();

            assertEquals(10, csvCityList.size());

            for (List<CsvCity> csvCitiesList : csvCityList) {
                assertEquals(100, csvCitiesList.size());
            }

            CsvCity csvCity = csvCityList.get(0).get(9);

            assertEquals(10L, csvCity.getId());
            assertEquals("Guangzhou", csvCity.getName());
            assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Guangzhou_Twin_Towers.jpg/500px-Guangzhou_Twin_Towers.jpg", csvCity.getImageUrl());
        }
    }
}
