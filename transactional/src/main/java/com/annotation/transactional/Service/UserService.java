package com.annotation.transactional.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;


@Service
public class UserService {
    
    @Transactional(propagation =  Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void updateUser() {
        boolean isTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        System.out.println("*****************************");
        System.out.println("Propagation Required: Is Transaction Active " + isTransactionActive);
        System.out.println("Propagation Required: Current Transaction Name " + currentTransactionName);
        System.out.println("*****************************");
        System.out.println("UPDATE QUERY TO update the user db values");
    }
}
