package com.example.authservice.service;

import com.example.authservice.dto.AnimalDTO;

public interface AnimalService {

    AnimalDTO createAnimal(AnimalDTO animalDTO);
    AnimalDTO updateAnimal(AnimalDTO animalDTO, Long id);
    void deleteAnimal(Long id);
    Iterable<AnimalDTO> getMyAnimals(String username);
    AnimalDTO getAnimalById(Long id);
}
