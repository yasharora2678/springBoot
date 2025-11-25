package com.exception.handle.Controller;

import java.io.IOException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exception.handle.Http.Exceptions.CustomException;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class UserController {
    @GetMapping(path = "/get-user")
    public ResponseEntity<?> getUser() {
        // try {
        throw new CustomException(HttpStatus.BAD_REQUEST, "Can't get user");
        // throw new NullPointerException("Throwing null pointer exception for
        // testing");
        // } catch (CustomException e) {
        // java.util.Map<String, Object> response = new java.util.HashMap<>();
        // response.put("timeStamp", new Date());
        // response.put("status", HttpStatus.BAD_REQUEST.value());
        // response.put("message", e.getMessage());
        // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        // }
    }

    @GetMapping(path = "/get-user-history")
    public ResponseEntity<?> getUserHistory() {
        throw new IllegalArgumentException("Can't get user history");
    }

    // ExceptionHandlerExceptionResolver has two annotations to handle -
    // ExceptionHanlder, ControllerAdvice
    @ExceptionHandler({CustomException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleCustomException(HttpServletResponse response, Exception e) throws IOException {
        java.util.Map<String, Object> message = new java.util.HashMap<>();
        message.put("timeStamp", new Date());
        message.put("status", HttpStatus.BAD_REQUEST.value());
        message.put("message-from-exception-handler", e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        // response.sendError(HttpStatus.BAD_REQUEST.value(), "Error Message");  // if we do not return responseentity otherwise it will be again modified by  response class while returning
    }
}