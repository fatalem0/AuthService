package com.example.authservice.controller;

import com.example.authservice.entity.UserEntity;
import com.example.authservice.exception.AnimalAlreadyExistsException;
import com.example.authservice.exception.AnimalNotFoundException;
import com.example.authservice.dto.AnimalDTO;
import com.example.authservice.exception.UnauthorizedUserException;
import com.example.authservice.exception.UserNotFoundException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AnimalService;
import com.example.authservice.util.Utils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals")
public class AnimalController {
    private final static Gson gson = new Gson();
    private final AnimalService animalService;
    private final UserRepository userRepository;
    private final Utils utils;

    @Autowired
    public AnimalController(AnimalService animalService, UserRepository userRepository, Utils utils) {
        this.animalService = animalService;
        this.userRepository = userRepository;
        this.utils = utils;
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAnimal(@RequestBody AnimalDTO animal,
                                               @RequestHeader("Authorization") String username)
            throws UserNotFoundException, UnauthorizedUserException, AnimalAlreadyExistsException {

        String decodedUsername = utils.getUsernameFromAuthorizationHeader(username)[0];
        UserEntity user = userRepository.findUserEntityByUsername(decodedUsername);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getIsAuthorized()) {
            throw new UnauthorizedUserException();
        }

        animalService.createAnimal(animal);
        String resp = "New animal created";

        return ResponseEntity.ok(gson.toJson(resp));
    }

    @PostMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateAnimal(@RequestBody AnimalDTO animalDTO,
                                               @PathVariable Long id,
                                               @RequestHeader(value = "Authorization") String username)
            throws UserNotFoundException, UnauthorizedUserException, AnimalNotFoundException {

        String decodedUsername = utils.getUsernameFromAuthorizationHeader(username)[0];
        UserEntity user = userRepository.findUserEntityByUsername(decodedUsername);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getIsAuthorized()) {
            throw new UnauthorizedUserException();
        }

        animalService.updateAnimal(animalDTO, id);
        String resp = "Animal with id " + id + " was updated";

        return ResponseEntity.ok(gson.toJson(resp));
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteAnimal(@PathVariable Long id,
                                               @RequestHeader("Authorization") String username)
            throws AnimalNotFoundException, UserNotFoundException, UnauthorizedUserException {

        String decodedUsername = utils.getUsernameFromAuthorizationHeader(username)[0];
        UserEntity user = userRepository.findUserEntityByUsername(decodedUsername);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getIsAuthorized()) {
            throw new UnauthorizedUserException();
        }

        animalService.deleteAnimal(id);
        String resp = "Animal with id " + id + " was deleted";
        return ResponseEntity.ok(gson.toJson(resp));
    }

    @GetMapping(path = "/my_animals", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMyAnimals(@RequestHeader("Authorization") String username) throws
            UserNotFoundException, UnauthorizedUserException, AnimalNotFoundException {

        String decodedUsername = utils.getUsernameFromAuthorizationHeader(username)[0];
        UserEntity user = userRepository.findUserEntityByUsername(decodedUsername);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getIsAuthorized()) {
            throw new UnauthorizedUserException();
        }

        Iterable<AnimalDTO> animals = animalService.getMyAnimals(decodedUsername);
        return ResponseEntity.ok(gson.toJson(animals));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAnimalById(@PathVariable Long id,
                                           @RequestHeader("Authorization") String username) throws
            UserNotFoundException, UnauthorizedUserException, AnimalNotFoundException {

        String decodedUsername = utils.getUsernameFromAuthorizationHeader(username)[0];
        UserEntity user = userRepository.findUserEntityByUsername(decodedUsername);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getIsAuthorized()) {
            throw new UnauthorizedUserException();
        }

        AnimalDTO animal = animalService.getAnimalById(id);
        return ResponseEntity.ok(gson.toJson(animal));
    }
}
