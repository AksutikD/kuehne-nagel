package com.logistic.kuehnenagel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CityGetDto {

    private Long id;
    private String name;
    private String imageUrl;
}
