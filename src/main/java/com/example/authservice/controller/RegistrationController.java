package com.example.authservice.controller;

import com.example.authservice.dto.UserDTO;
import com.example.authservice.exception.UserAlreadyExistsException;
import com.example.authservice.service.AuthenticationService;
import com.example.authservice.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    private final static Gson gson = new Gson();
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public RegistrationController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerNewUser(@RequestBody UserDTO userDTO)
            throws UserAlreadyExistsException {

            userService.registerNewUserAccount(userDTO);
            authenticationService.login(userDTO);
            String resp = "New user registered and authorized";

            return ResponseEntity.ok(gson.toJson(resp));
    }
}
