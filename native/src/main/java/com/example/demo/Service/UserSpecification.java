package com.example.demo.Service;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.Entity.UserDetails;

import jakarta.persistence.criteria.JoinType;

public class UserSpecification {

    public static Specification<UserDetails> likeName(String name){
        return (root, query , cb) ->
                cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<UserDetails> joinAddress() {
        return (root, query , cb) -> {
            root.join("userAddress", JoinType.INNER); // join but do not filter
            return cb.conjunction();  // always true
        };
    }
}

