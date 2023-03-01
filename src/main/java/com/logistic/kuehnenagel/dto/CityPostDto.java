package com.logistic.kuehnenagel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.web.multipart.MultipartFile;

@Jacksonized
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CityPostDto {

    @JsonProperty("old_name")
    private String oldName;

    @JsonProperty("new_name")
    private String newName;

    @JsonProperty("file")
    private MultipartFile multipartFile;
}
