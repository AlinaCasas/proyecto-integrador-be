package com.proyecto.integrador.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.proyecto.integrador.user.Role.ADMIN;
import static com.proyecto.integrador.user.Role.USER;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] PUBLIC_LIST_URL = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(PUBLIC_LIST_URL)
                                .permitAll()
                                .requestMatchers(GET, "/api/v1/products/**").permitAll()
                                .requestMatchers(GET, "/api/v1/search/**").permitAll()
                                .requestMatchers(POST, "/api/v1/products/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(POST, "/api/v1/products/images/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(PUT, "/api/v1/products/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(DELETE, "/api/v1/products/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/reservations/admin/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/reservations/**").hasAnyRole(USER.name(), ADMIN.name())
                                .requestMatchers("/api/v1/reviews/admin/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(GET, "/api/v1/reviews/product/**").permitAll()
                                .requestMatchers("/api/v1/reviews/**").hasAnyRole(USER.name(), ADMIN.name())
                                .requestMatchers(GET, "/api/v1/categories/**").permitAll()
                                .requestMatchers(GET, "/api/v1/favorites/**").hasAnyRole(USER.name(), ADMIN.name())
                                .requestMatchers(POST, "/api/v1/favorites/add").hasAnyRole(USER.name(), ADMIN.name())
                                .requestMatchers(DELETE, "/api/v1/favorites/remove").hasAnyRole(USER.name(), ADMIN.name())
                                .requestMatchers(GET, "/api/v1/favorites/list/**").hasAnyRole(USER.name(), ADMIN.name())
                                .requestMatchers(POST, "/api/v1/characteristic").hasAnyRole(ADMIN.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
