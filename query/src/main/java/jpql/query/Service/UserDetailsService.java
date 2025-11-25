package jpql.query.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jpql.query.Entity.UserDetails;
import jpql.query.Repository.UserDetailsRepository;

@Service
public class UserDetailsService {
    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Transactional
    public UserDetails addUser(UserDetails user) {
        return userDetailsRepository.save(user);
    }

    public List<UserDetails> getUsers(String name) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").descending());
        if(name != null && !name.isEmpty()) {
            return userDetailsRepository.findUserDetailsByName(name, pageable);
        }
        else {
            return userDetailsRepository.findAll();
        }
    } 

    public List<UserDetails> getUsersAnd(String name, String phone) {
        return userDetailsRepository.findUserDetailsByNameAndPhone(name, phone);
    } 

    public List<UserDetails> getUsersOr(String name, String phone) {
        return userDetailsRepository.findUserDetailsByNameOrPhone(name, phone);
    } 

    public List<UserDetails> getUsersLike(String name) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").descending());
        return userDetailsRepository.findUserDetailsByNameContaining(name, pageable);
    } 

    @Transactional
    public String deleteUser(Long id) {
        userDetailsRepository.deleteByUserId(id);
        return "User Deleted Successfully";
    }
}