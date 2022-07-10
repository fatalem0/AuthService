package com.example.authservice.exception;

import com.example.authservice.dto.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFound() {
        String error = "User not found";
        return buildResponseEntity(new ApiErrorDTO(HttpStatus.NOT_FOUND, error));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExists() {
        String error = "User already exists";
        return buildResponseEntity(new ApiErrorDTO(HttpStatus.CONFLICT, error));
    }

    @ExceptionHandler(AnimalNotFoundException.class)
    protected ResponseEntity<Object> handleAnimalNotFound() {
        String error = "Animal not found";
        return buildResponseEntity(new ApiErrorDTO(HttpStatus.NOT_FOUND, error));
    }

    @ExceptionHandler(AnimalAlreadyExistsException.class)
    protected ResponseEntity<Object> handleAnimalAlreadyExists() {
        String error = "Animal already exists";
        return buildResponseEntity(new ApiErrorDTO(HttpStatus.CONFLICT, error));
    }

    @ExceptionHandler(WrongPasswordException.class)
    protected ResponseEntity<Object> handleWrongUsernameOrPassword() {
        String error = "Wrong password, please try again";
        return buildResponseEntity(new ApiErrorDTO(HttpStatus.BAD_REQUEST, error));
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    protected ResponseEntity<Object> handleUnauthorizedUser() {
        String error = "Unauthorized user";
        return buildResponseEntity(new ApiErrorDTO(HttpStatus.UNAUTHORIZED, error));
    }

    @ExceptionHandler(LockedException.class)
    protected ResponseEntity<Object> handleLockedUser() {
        String error = "Your account has been locked due to 10 failed attempts. It will be unlocked after 1 hour";
        return buildResponseEntity(new ApiErrorDTO(HttpStatus.FORBIDDEN, error));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorDTO apiErrorDTO) {
        return new ResponseEntity<>(apiErrorDTO, apiErrorDTO.getStatus());
    }
}
