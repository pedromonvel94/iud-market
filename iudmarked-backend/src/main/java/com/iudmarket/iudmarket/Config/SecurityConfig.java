package com.iudmarket.iudmarket.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Lectura y cobros: ADMIN y CAJERA
                        .requestMatchers(HttpMethod.GET, "/api/cajeras/disponibles").hasAnyRole("ADMIN", "CAJERA")
                        .requestMatchers(HttpMethod.POST, "/api/compras/procesar").hasAnyRole("ADMIN", "CAJERA")
                        .requestMatchers(HttpMethod.GET, "/api/compras", "/api/compras/**").hasAnyRole("ADMIN", "CAJERA")
                        .requestMatchers(HttpMethod.GET, "/api/cajeras", "/api/cajeras/*").hasAnyRole("ADMIN", "CAJERA")
                        .requestMatchers(HttpMethod.GET, "/api/clientes", "/api/clientes/*").hasAnyRole("ADMIN", "CAJERA")
                        .requestMatchers(HttpMethod.GET, "/api/productos", "/api/productos/*").hasAnyRole("ADMIN", "CAJERA")
                        // Solo ADMIN: cambiar estado de cajera y mutaciones CRUD
                        .requestMatchers(HttpMethod.PUT, "/api/cajeras/*/estado").hasRole("ADMIN")
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
