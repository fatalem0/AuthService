package com.example.authservice.service.impl;

import com.example.authservice.entity.UserEntity;
import com.example.authservice.exception.UserAlreadyExistsException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final UserRepository userRepository;

    @Autowired
    public ValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean checkValidUsername(String username) throws UserAlreadyExistsException {
        UserEntity user = userRepository.findUserEntityByUsername(username);

        if (user != null) {
            throw new UserAlreadyExistsException();
        }

        return true;
    }
}
