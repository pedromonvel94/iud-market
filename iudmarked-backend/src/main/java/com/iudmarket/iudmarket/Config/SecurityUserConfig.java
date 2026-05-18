package com.iudmarket.iudmarket.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Usuarios en memoria para HTTP Basic (demo local y pruebas Postman).
 */
@Configuration
public class SecurityUserConfig {

    @Value("${app.security.admin.username}")
    private String adminUsername;

    @Value("${app.security.admin.password}")
    private String adminPassword;

    @Value("${app.security.cajera.username}")
    private String cajeraUsername;

    @Value("${app.security.cajera.password}")
    private String cajeraPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        UserDetails cajera = User.builder()
                .username(cajeraUsername)
                .password(passwordEncoder.encode(cajeraPassword))
                .roles("CAJERA")
                .build();

        return new InMemoryUserDetailsManager(admin, cajera);
    }
}
