package com.logistic.kuehnenagel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CityGetDTO {

    private Long id;
    private String name;
    private String imageUrl;
}
