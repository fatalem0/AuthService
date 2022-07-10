package com.example.authservice.service.impl;

import com.example.authservice.dto.UserDTO;
import com.example.authservice.entity.AnimalEntity;
import com.example.authservice.entity.UserEntity;
import com.example.authservice.exception.UserAlreadyExistsException;
import com.example.authservice.exception.UserNotFoundException;
import com.example.authservice.repository.AnimalRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    public static final int MAX_FAILED_ATTEMPTS = 10;

    private static final long LOCK_TIME_DURATION = 60 * 60 * 1000; // 1 hour

    private final UserRepository userRepository;

    private final AnimalRepository animalRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AnimalRepository animalRepository) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
    }

    @Override
    public void increaseFailedAttempts(UserDTO userDTO) {
        UserEntity user = userRepository.findUserEntityByUsername(userDTO.getUsername());
        int newFailAttempts = user.getFailedAttempt() + 1;
        user.setFailedAttempt(newFailAttempts);
        userRepository.save(user);
    }

    @Override
    public void resetFailedAttempts(String username) {
        UserEntity user = userRepository.findUserEntityByUsername(username);
        user.setFailedAttempt(0);
        userRepository.save(user);
    }

    @Override
    public void lock(UserDTO userDTO) {
        UserEntity user = userRepository.findUserEntityByUsername(userDTO.getUsername());
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public boolean unlockWhenTimeExpired(UserDTO userDTO) {
        UserEntity user = userRepository.findUserEntityByUsername(userDTO.getUsername());
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);

            userRepository.save(user);

            return true;
        }

        return false;
    }

    @Override
    public UserDTO registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistsException {
        UserEntity user = userRepository.findUserEntityByUsername(userDTO.getUsername());

        if (user != null) {
            throw new UserAlreadyExistsException();
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);

        for (String animal : userDTO.getAnimals()) {
            AnimalEntity animalEntity = animalRepository.findAnimalEntityByName(animal);
            if (animalEntity != null) {
                userEntity.getAnimals().add(animalEntity);
            }
        }

        userRepository.save(userEntity);

        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserEntityByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
