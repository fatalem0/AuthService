package com.example.authservice.service;

import com.example.authservice.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService  {
    void increaseFailedAttempts(UserDTO userDTO);
    void resetFailedAttempts(String username);
    void lock(UserDTO userDTO);
    boolean unlockWhenTimeExpired(UserDTO userDTO);

    UserDTO registerNewUserAccount(UserDTO userDTO);
}
