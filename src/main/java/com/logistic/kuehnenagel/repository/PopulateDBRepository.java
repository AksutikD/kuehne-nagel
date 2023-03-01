package com.logistic.kuehnenagel.repository;

import com.logistic.kuehnenagel.domain.City;
import org.springframework.stereotype.Repository;

@Repository
public interface PopulateDBRepository<T extends City> extends org.springframework.data.repository.Repository<T, Long> {

    Iterable<T> saveAll(Iterable<T> entities);
}
