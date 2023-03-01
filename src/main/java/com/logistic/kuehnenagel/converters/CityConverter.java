package com.logistic.kuehnenagel.converters;

import com.google.common.util.concurrent.RateLimiter;
import com.logistic.kuehnenagel.domain.City;
import com.logistic.kuehnenagel.domain.csv.CsvCity;
import com.logistic.kuehnenagel.dto.CityGetDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
public class CityConverter {

    public static CityGetDTO cityToGetDtoConvert(final City city) {
        return new CityGetDTO(city.getName(), city.getImage());
    }

    public static City csvCityToCityConvert(final CsvCity csvCity) {
        City city = new City(csvCity.getId(), csvCity.getName(), null);

        try {
            URL url = new URL(csvCity.getPhoto());
            try (InputStream inputStream = new ThrottledInputStream(url.openStream())) {
                byte[] imageBytes = inputStream.readAllBytes();
                city.setImage(imageBytes);
            }
        } catch (IOException e) {
            if (e.getMessage().contains("Server returned HTTP response code: 429")) {
                try {
                    log.info("Trying to redownload image");
                    Thread.sleep(1000);
                    csvCityToCityConvert(csvCity);
                } catch (InterruptedException interruptedException) {

                }
            } else {
                log.info("Skipping invalid image for " + city.getId() + " reason: " + e);
            }
        }

        return city;
    }

    private static class ThrottledInputStream extends InputStream {
        private final InputStream inputStream;
        private final RateLimiter rateLimiter;

        public ThrottledInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            //Rate limit for 500 KB per second
            this.rateLimiter = RateLimiter.create(500000.0);
        }

        @Override
        public int read() throws IOException {
            rateLimiter.acquire(1);
            return inputStream.read();
        }
    }
}
