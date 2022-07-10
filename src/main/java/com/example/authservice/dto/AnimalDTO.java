package com.example.authservice.dto;

import com.example.authservice.enums.Gender;
import com.example.authservice.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Data
@Component
public class AnimalDTO {
    @JsonProperty("type")
    private Type type;
    @JsonProperty("date_of_birth")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("name")
    private String name;
    @JsonProperty("owners")
    private Set<String> owners;
}
