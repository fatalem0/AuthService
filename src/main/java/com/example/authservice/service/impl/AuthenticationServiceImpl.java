package com.example.authservice.service.impl;

import com.example.authservice.dto.UserDTO;
import com.example.authservice.entity.UserEntity;
import com.example.authservice.exception.UserNotFoundException;
import com.example.authservice.exception.WrongPasswordException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthenticationService;
import com.example.authservice.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
//@Primary
public class AuthenticationServiceImpl implements AuthenticationService {

    private final static Gson gson = new Gson();

    private final UserRepository userRepository;

    private final UserService userService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<String> login(UserDTO userDTO) throws UsernameNotFoundException {
        boolean isAuthorized;

        UserEntity user = userRepository.findUserEntityByUsername(userDTO.getUsername());

        if (user == null) {
            throw new UserNotFoundException();
        }

        UserEntity comparableEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, comparableEntity);

        if (Objects.equals(comparableEntity.getPassword(), user.getPassword())) {
            user.setIsAuthorized(true);
            userRepository.save(user);

            if (user.getFailedAttempt() > 0) {
                userService.resetFailedAttempts(user.getUsername());
            }
            isAuthorized = true;
        }
        else {
            isAuthorized = false;
        }

        if (!isAuthorized) {
            System.out.println("isAuthorized");
            if (user.isAccountNonLocked()) {
                System.out.println(user.getFailedAttempt());
                if (user.getFailedAttempt() <= UserServiceImpl.MAX_FAILED_ATTEMPTS) {
                    userService.increaseFailedAttempts(userDTO);
                    throw new WrongPasswordException();
                }
                else {
                    System.out.println("lock");
                    userService.lock(userDTO);
                    throw new com.example.authservice.exception.LockedException();
                }
            }
            else {
                if (userService.unlockWhenTimeExpired(userDTO)) {
                    String resp = "Your account has been unlocked. Please try to login again";
                    return ResponseEntity.ok(gson.toJson(resp));
                }
                else {
                    throw new com.example.authservice.exception.LockedException();
                }
            }
        }

        String resp = "You are logged in";
        return ResponseEntity.ok(gson.toJson(resp));
    }
}
