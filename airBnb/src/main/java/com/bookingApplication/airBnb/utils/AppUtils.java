package com.bookingApplication.airBnb.utils;

import com.bookingApplication.airBnb.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtils {
    public static UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }
}
