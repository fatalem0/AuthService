package com.example.authservice.service.impl;

import com.example.authservice.exception.AnimalAlreadyExistsException;
import com.example.authservice.exception.AnimalNotFoundException;
import com.example.authservice.dto.AnimalDTO;
import com.example.authservice.entity.AnimalEntity;
import com.example.authservice.entity.UserEntity;
import com.example.authservice.repository.AnimalRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AnimalService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository, UserRepository userRepository) {
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AnimalDTO createAnimal(AnimalDTO animalDTO) throws AnimalAlreadyExistsException {
        AnimalEntity animal = animalRepository.findAnimalEntityByName(animalDTO.getName());

        if (animal != null) {
            throw new AnimalAlreadyExistsException();
        }

        AnimalEntity animalEntity = new AnimalEntity();
        BeanUtils.copyProperties(animalDTO, animalEntity);

        for (String owner : animalDTO.getOwners()) {
            UserEntity user = userRepository.findUserEntityByUsername(owner);
            if (user != null) {
                user.getAnimals().add(animalEntity);
            }
        }

        animalRepository.save(animalEntity);

        return animalDTO;
    }

    @Override
    public AnimalDTO updateAnimal(AnimalDTO animalDTO, Long id) throws AnimalNotFoundException {
        AnimalEntity animalEntity = animalRepository.findAnimalEntityById(id);

        if (animalEntity == null) {
            throw new AnimalNotFoundException();
        }

        BeanUtils.copyProperties(animalDTO, animalEntity);

        for (UserEntity owner : animalEntity.getOwners()) {
            owner.getAnimals().remove(animalEntity);
            animalEntity.setOwners(new HashSet<>());
        }

        for (String owner : animalDTO.getOwners()) {
            UserEntity user = userRepository.findUserEntityByUsername(owner);
            if (user != null) {
                user.getAnimals().add(animalEntity);
            }
        }

        animalRepository.save(animalEntity);

        return animalDTO;
    }

    @Override
    public void deleteAnimal(Long id) throws AnimalNotFoundException {
        AnimalEntity animal = animalRepository.findAnimalEntityById(id);

        if (animal == null) {
            throw new AnimalNotFoundException();
        }

        for (UserEntity owner : animal.getOwners()) {
            owner.getAnimals().remove(animal);
            animal.setOwners(new HashSet<>());
        }

        animalRepository.delete(animal);
    }

    @Override
    public Iterable<AnimalDTO> getMyAnimals(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserEntityByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        Iterable<AnimalEntity> animalEntities = user.getAnimals();
        List<AnimalDTO> listAnimals = new ArrayList<>();

        for (AnimalEntity animalEntity : animalEntities) {
            AnimalDTO returnValue = new AnimalDTO();
            BeanUtils.copyProperties(animalEntity, returnValue);
            listAnimals.add(returnValue);
        }

        return listAnimals;
    }

    @Override
    public AnimalDTO getAnimalById(Long id) throws AnimalNotFoundException {
        AnimalEntity animal = animalRepository.findAnimalEntityById(id);

        if (animal == null) {
            throw new AnimalNotFoundException();
        }

        AnimalDTO returnValue = new AnimalDTO();
        BeanUtils.copyProperties(animal, returnValue);

        return returnValue;
    }
}
