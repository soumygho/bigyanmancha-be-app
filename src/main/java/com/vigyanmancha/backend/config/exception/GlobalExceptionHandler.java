package com.vigyanmancha.backend.config.exception;

import com.vigyanmancha.backend.dto.response.ErrorResponse;
import com.vigyanmancha.backend.exceptions.ConflictException;
import com.vigyanmancha.backend.exceptions.ResourceNotFoundException;
import com.vigyanmancha.backend.utility.ExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException ex) {
        return new ResponseEntity<>(ExceptionFactory.create(ex, 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return new ResponseEntity<>(ExceptionFactory.create(ex, 401), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGeneral(ResourceNotFoundException ex) {

        return new ResponseEntity<>(ExceptionFactory.create(ex, 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleGeneral(ConflictException ex) {

        return new ResponseEntity<>(ExceptionFactory.create(ex, 400), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGeneral(RuntimeException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(ExceptionFactory.create(ex, 500), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

