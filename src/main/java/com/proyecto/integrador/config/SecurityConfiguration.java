package com.proyecto.integrador.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(PUBLIC_LIST_URL)
                                .permitAll()
                                .requestMatchers(GET, "/api/v1/products/**").permitAll()
                                .requestMatchers(POST, "/api/v1/products/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(POST, "/api/v1/products/images/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(PUT, "/api/v1/products/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(DELETE, "/api/v1/products/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/reservations/admin/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/reservations/**").hasAnyRole(USER.name(), ADMIN.name())
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Content-Type", "multipart/form-data");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}
