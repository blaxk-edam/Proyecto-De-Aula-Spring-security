package com.reciclaje.proyectodeaula.config;

import com.reciclaje.proyectodeaula.security.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UsuarioDetailsService usuarioDetailsService,
                                                              PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            String rol = authentication.getAuthorities().iterator().next().getAuthority();

            switch (rol) {
                case "ROLE_CIUDADANO" -> response.sendRedirect("/ciudadano");
                case "ROLE_RECICLADOR" -> response.sendRedirect("/reciclador");
                case "ROLE_ACOPIO" -> response.sendRedirect("/acopio");
                default -> response.sendRedirect("/");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/registro", "/registrar", "/error-403", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/ciudadano/**", "/crear-solicitud").hasRole("CIUDADANO")
                        .requestMatchers("/reciclador/**", "/completar/**").hasRole("RECICLADOR")
                        .requestMatchers("/acopio/**").hasRole("ACOPIO")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .usernameParameter("usuario")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler())
                        .failureUrl("/?error")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/?logout")
                        .permitAll())
                .exceptionHandling(ex -> ex.accessDeniedPage("/error-403"));

        return http.build();
    }
}
