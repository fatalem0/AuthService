package com.example.authservice.repository;

import com.example.authservice.entity.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {
    AnimalEntity findAnimalEntityById(Long id);
    AnimalEntity findAnimalEntityByName(String name);

}
