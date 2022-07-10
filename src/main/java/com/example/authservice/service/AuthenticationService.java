package com.example.authservice.service;

import com.example.authservice.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<String> login(UserDTO userDTO);
}
