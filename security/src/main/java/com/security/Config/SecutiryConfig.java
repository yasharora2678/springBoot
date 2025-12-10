package com.security.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecutiryConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/users").hasAnyRole("USER", "ADMIN") // allow attacker
                                                                                                       // page
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session.maximumSessions(1)
                                                .maxSessionsPreventsLogin(true))
                                .formLogin(Customizer.withDefaults());

                return http.build();
        }
}
