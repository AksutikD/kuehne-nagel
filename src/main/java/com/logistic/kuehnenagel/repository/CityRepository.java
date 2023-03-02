package com.logistic.kuehnenagel.repository;

import com.logistic.kuehnenagel.domain.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends org.springframework.data.repository.Repository<City, Long> {

    void saveAll(Iterable<City> entities);

    Optional<City> findById(Long id);

    Page<City> findAll(Pageable pageable);

    Page<City> findAllByName(String name, Pageable pageable);
}
