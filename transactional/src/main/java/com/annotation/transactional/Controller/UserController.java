package com.annotation.transactional.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.transactional.Service.UserService;

@RestController
@RequestMapping(value = "/api/")
public class UserController {

    private final PlatformTransactionManager txManager;

    public UserController(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<String> updateUser() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("UserUpdateTx");
        TransactionStatus status = txManager.getTransaction(def);
        System.out.println(status + "Status");
        try {
            userService.updateUser();
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
        }
        return ResponseEntity.status(HttpStatus.OK).body("User details upated");
    }
}
