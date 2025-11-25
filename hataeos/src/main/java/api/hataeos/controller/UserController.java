package api.hataeos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.hataeos.interfaces.UserResponse;
import api.hataeos.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUserDetails() {
        UserResponse response = userService.getUser();

        // creating link using HATAEOS principle
        Link verifyLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash("sms-verify-builder")
                .slash(response.getUserId())
                .withRel("verify")
                .withType("POST");

        response.add(verifyLink);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
