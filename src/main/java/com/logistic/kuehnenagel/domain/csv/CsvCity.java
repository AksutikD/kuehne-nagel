package com.logistic.kuehnenagel.domain.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;

@Getter
public class CsvCity {

    @CsvBindByName
    private Long id;

    @CsvBindByName
    private String name;

    @CsvBindByName
    private String photo;
}
