package api.hataeos.service;

import org.springframework.stereotype.Service;

import api.hataeos.interfaces.UserResponse;

@Service
public class UserService {
    
    public UserResponse getUser() {
        UserResponse response = new UserResponse();
        response.setUserId("1");
        response.setName("Yash");
        response.setVerifyStatus("Not Verified");
        return response;
    }
}
