package com.example.authservice.controller;

import com.example.authservice.exception.UserAlreadyExistsException;
import com.example.authservice.service.ValidationService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
public class ValidationController {
    private final static Gson gson = new Gson();
    private final ValidationService validationService;

    @Autowired
    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkValidUsername(@RequestBody ObjectNode username)
            throws UserAlreadyExistsException {

        validationService.checkValidUsername(username.get("username").asText());
        String resp = "The username is free";

        return ResponseEntity.ok(gson.toJson(resp));
    }
}
