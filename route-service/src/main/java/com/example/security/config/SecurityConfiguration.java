package com.example.security.config;

import com.example.model.utils.Role;
import com.example.security.filters.AuthentitcationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private AuthenticationProvider authenticationProvider;
    private AuthentitcationFilter authentitcationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers("/routes/saved").hasAuthority(Role.USER.toString())
                            .requestMatchers("/routes/{id}/save").hasAuthority(Role.USER.toString())
                            .requestMatchers("/auth/me").hasAuthority(Role.USER.toString())
                            .anyRequest().permitAll();
                }).sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).authenticationProvider(authenticationProvider).addFilterAt(
                        authentitcationFilter, UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
