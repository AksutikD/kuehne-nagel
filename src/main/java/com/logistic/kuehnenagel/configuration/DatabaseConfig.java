package com.logistic.kuehnenagel.configuration;

import com.logistic.kuehnenagel.converters.CityConverter;
import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.domain.csv.CsvCity;
import com.logistic.kuehnenagel.service.CityService;
import com.logistic.kuehnenagel.service.CsvService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;


@Configuration
@Profile({"local", "test"})
@Slf4j
@RequiredArgsConstructor
public class DatabaseConfig {

    @Value("classpath:static/cities.csv")
    Resource resourceFile;

    private final CityService cityService;
    private final CsvService csvService;

    @PostConstruct
    public void populateDatabase() throws IOException {
        log.info("Started DB population");

        if (resourceFile.exists()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(resourceFile.getInputStream()))) {
                Stream<List<CsvCity>> csvCityStream = csvService.getStreamFromCSV(reader, CsvCity.class);

                for (List<CsvCity> csvCitiesList : csvCityStream.toList()) {
                    List<City> newCities = csvCitiesList.stream()
                            .filter(csvCity -> csvCity.getImageUrl().length() <= 255)
                            .map(CityConverter::csvCityToCityConvert)
                            .toList();
                    cityService.createCities(newCities);
                }
            }
        }

        log.info("DB population is finished");
    }
}
