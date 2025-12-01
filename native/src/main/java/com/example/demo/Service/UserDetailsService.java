package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.UserAddress;
import com.example.demo.Entity.UserDetails;
import com.example.demo.Repository.UserDetailsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class UserDetailsService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Transactional
    public UserDetails addUser(UserDetails user) {
        return userDetailsRepository.save(user);
    }
    // In this we have used Native Query
    public List<Object[]> getUsers(String name) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT ud.name AS name , ud.phone AS phone, ua.city AS city, ua.street AS street ");
        queryBuilder.append("FROM user_details ud ");
        queryBuilder.append("JOIN user_address ua ON ud.user_address_id = ua.id ");
        queryBuilder.append("WHERE 1=1 ");

        List<Object> parameters = new ArrayList<>();

        // Dynamically add conditions
        if (name != null && !name.isEmpty()) {
            queryBuilder.append("AND ud.name = ?");
            parameters.add(name);
        }

        // sorting
        queryBuilder.append("ORDER BY ").append("ud.name").append(" DESC");

        // pagination
        int size = 5;
        int page = 0;
        queryBuilder.append(" LIMIT ? OFFSET ? ");
        parameters.add(size);
        parameters.add(page * size);

        // Create native query
        Query nativeQuery = entityManager.createNativeQuery(queryBuilder.toString());

        // Set the parameters for the query
        for (int i = 0; i < parameters.size(); i++) {
            nativeQuery.setParameter(i + 1, parameters.get(i));
        }

        // Execute and get the results
        List<Object[]> result = nativeQuery.getResultList();

        return result;
        // if (name != null && !name.isEmpty()) {
        // return userDetailsRepository.getUserDetailsByNameNativeQuery(name);
        // } else {
        // return userDetailsRepository.findAll();
        // }
    }

    public List<UserDetails> getUserDetailsByPhoneCriteriaAPI(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<UserDetails> crQuery = cb.createQuery(UserDetails.class); // what my each row would be look like ,
                                                                                // so it will look like that each row
                                                                                // would be userDetails

        Root<UserDetails> user = crQuery.from(UserDetails.class); // from clause

        crQuery.multiselect(user.get("name"), user.get("phone")); // select *

        Predicate predicate = cb.like(user.get("name"), name + "%"); // where clause
        crQuery.where(predicate);

        TypedQuery<UserDetails> query = entityManager.createQuery(crQuery);
        List<UserDetails> output = query.getResultList();

        return output;
    }

    // In this we have used CRITERIA API 
    public List<UserDTO> getSpecificUserDetailsByPhoneCriteriaAPI(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> crQuery = cb.createQuery(Object[].class); // what my each row would be look like , so it
                                                                          // will look like that each row would be
                                                                          // userDetails

        Root<UserDetails> user = crQuery.from(UserDetails.class); // from clause

        Join<UserDetails, UserAddress> address = user.join("userAddress", JoinType.INNER); // Perform join using
                                                                                           // Criteria API

        crQuery.multiselect(user.get("name"), user.get("phone"), address.get("city"), address.get("street")); // select
                                                                                                              // *

        Predicate predicate = cb.like(user.get("name"), name + "%"); // where clause
        crQuery.where(predicate);

        crQuery.orderBy(cb.desc(user.get("name")));

        TypedQuery<Object[]> query = entityManager.createQuery(crQuery);

        query.setFirstResult(0); // This is the page no
        query.setMaxResults(5); // This is the limit

        List<Object[]> output = query.getResultList();

        // Processing results
        List<UserDTO> results = new ArrayList<>();
        for (Object[] row : output) {
            String userName = (String) row[0];
            String phone = (String) row[1];
            String city = (String) row[2];
            String street = (String) row[3];
            UserDTO result = new UserDTO(userName, phone, city, street);
            results.add(result);
        }
        return results;
    }
}
