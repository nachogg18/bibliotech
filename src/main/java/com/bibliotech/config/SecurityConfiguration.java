package com.bibliotech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.authorizeHttpRequests(
                (authorize) -> authorize.requestMatchers("/insecured").permitAll()
        );

        http.authorizeHttpRequests(
                (authorize) -> authorize.requestMatchers("/secured").denyAll()
        );


        return http.build();
    }



}
