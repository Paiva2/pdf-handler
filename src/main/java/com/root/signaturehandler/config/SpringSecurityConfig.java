package com.root.signaturehandler.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {
        DefaultSecurityFilterChain securityConfig = http.cors()
                .configurationSource(c -> {
                    CorsConfiguration cfg = new CorsConfiguration();
                    cfg.addAllowedMethod(CorsConfiguration.ALL);
                    cfg.addAllowedOrigin("*");
                    cfg.addAllowedHeader("*");

                    return cfg;
                })
                .and().csrf().disable()
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.antMatchers(HttpMethod.POST, "/api/v1/user/login").permitAll();
                    req.antMatchers(HttpMethod.POST, "/api/v1/user/register").permitAll();
                    req.antMatchers(HttpMethod.PATCH, "/api/v1/user/forgot-password").permitAll();
                    req.antMatchers(HttpMethod.POST, "/api/v1/generic/upload-image").permitAll();
                    req.anyRequest().authenticated();
                }).addFilterBefore(this.securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

        return securityConfig;
    }


}
