package com.northpole.journalappserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration  {
    @Value("${northpole.api-key.key}")
    private String principalRequestHeader;

    @Value("${northpole.api-key.value}")
    private String principalRequestValue;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ApiKeyAuthFilter apiKeyAuthFilter=new ApiKeyAuthFilter(principalRequestHeader);

        apiKeyAuthFilter.setAuthenticationManager((Authentication authentication) -> {
            String principal = (String) authentication.getPrincipal();
            if (!principalRequestValue.equals(principal))
                throw new BadCredentialsException("Invalid/Unacceptable API Key");

            authentication.setAuthenticated(true);
            return authentication;
        });

        return http.csrf((csrf)->csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}
