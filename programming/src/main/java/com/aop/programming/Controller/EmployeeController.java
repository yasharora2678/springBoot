package com.aop.programming.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class EmployeeController {
    
    @GetMapping (path= "/fetchEmployee")
    public ResponseEntity<String> fetchEmployee() {
        return ResponseEntity.status(HttpStatus.OK).body("Employee Details Fetched");
    }
}
