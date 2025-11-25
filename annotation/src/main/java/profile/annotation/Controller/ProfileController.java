package profile.annotation.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@Profile("dev")
public class ProfileController {
    @Value("${applicationUsername}")
    String applicationUsername;

    @Value("${password}")
    String password;

    public ProfileController() { 
        System.out.println("Profile Controller Initialized");
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUserDetails() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("Username - " + applicationUsername + " Password - " + password);
    }
}
