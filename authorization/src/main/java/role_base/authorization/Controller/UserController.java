package role_base.authorization.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import role_base.authorization.Entity.UserRegisterEntity;
import role_base.authorization.Services.UserRegisterEntityService;


@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRegisterEntityService userRegisterEntityService;

    @Autowired 
    PasswordEncoder passwordEncoder;

    // using this api to register username, password and role
    @PostMapping("/user-register")
    public ResponseEntity<String> register(@RequestBody UserRegisterEntity userRegisterDetails) {
        // Hash the password before handling
        userRegisterDetails.setPassword(passwordEncoder.encode(userRegisterDetails.getPassword()));

        userRegisterEntityService.save(userRegisterDetails);

        return ResponseEntity.ok("User Created Successfully");
    }


    @GetMapping("/user")
    public ResponseEntity<String> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("User fetched Successfully");
    }
}
