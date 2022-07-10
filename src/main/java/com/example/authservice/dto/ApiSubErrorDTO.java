package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiSubErrorDTO {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ApiSubErrorDTO(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
