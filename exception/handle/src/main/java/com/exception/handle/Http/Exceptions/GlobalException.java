package com.exception.handle.Http.Exceptions;

import java.io.IOException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<?> handleCustomException(Exception e) throws IOException {
        java.util.Map<String, Object> message = new java.util.HashMap<>();
        message.put("timeStamp", new Date());
        message.put("status", HttpStatus.BAD_REQUEST.value());
        message.put("message-from-global-handler", e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
