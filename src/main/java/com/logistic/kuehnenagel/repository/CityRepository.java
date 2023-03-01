package com.logistic.kuehnenagel.repository;

import com.logistic.kuehnenagel.domain.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends org.springframework.data.repository.Repository<City, Long> {

    boolean existsByName(String name);

    City save(City city);

    Iterable<City> saveAll(Iterable<City> entities);

    Optional<City> findByName(String name);

    Page<City> findAll(Pageable pageable);
}
