package com.example.authservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class ApiErrorDTO {
    private static final AtomicInteger count = new AtomicInteger(0);
    private final int id;
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;

    public ApiErrorDTO(HttpStatus status, String message) {
        this.id = count.incrementAndGet();
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }
}
