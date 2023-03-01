package com.logistic.kuehnenagel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CityPostDto {

    @Positive
    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters long")
    private String name;

    @NotBlank
    @Size(min = 12, max = 255, message = "Name must be between 12 and 255 characters long")
    private String imageUrl;
}
