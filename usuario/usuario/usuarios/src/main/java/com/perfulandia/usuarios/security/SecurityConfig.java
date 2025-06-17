package com.perfulandia.usuarios.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)  // deshabilitar CSRF para simplificar pruebas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/usuarios/register", "/api/usuarios/login").permitAll()  // permisos públicos
                .anyRequest().authenticated()  // el resto necesita autenticación
            )
            .httpBasic(httpBasic -> {}); // usar basic auth, aunque luego podrías cambiar a JWT en filtros
        return http.build();
    }
}
