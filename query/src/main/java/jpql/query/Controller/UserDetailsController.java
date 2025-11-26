package jpql.query.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpql.query.Entity.UserDetails;
import jpql.query.Service.UserDetailsService;

@RestController
@RequestMapping("/api/user")
public class UserDetailsController {
    @Autowired
    UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<UserDetails> createUser(@RequestBody UserDetails user) {
        UserDetails response = userDetailsService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserDetails>> getUsers(@RequestParam(required = false) String name) {
        List<UserDetails> response = userDetailsService.getUsers(name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/and")
    public ResponseEntity<List<UserDetails>> getUsersAnd(@RequestParam(required = false) String name, @RequestParam(required = false) String phone) {
        List<UserDetails> response = userDetailsService.getUsersAnd(name, phone);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/or")
    public ResponseEntity<List<UserDetails>> getUsersOr(@RequestParam(required = false) String name, @RequestParam(required = false) String phone) {
        List<UserDetails> response = userDetailsService.getUsersOr(name, phone);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/like")
    public ResponseEntity<List<UserDetails>> getUsersLike(
        // @RequestParam(required = false) String name
    ) {
        // List<UserDetails> response = userDetailsService.getUsersLike(name);
        List<UserDetails> response = userDetailsService.getUsersLike();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = userDetailsService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
