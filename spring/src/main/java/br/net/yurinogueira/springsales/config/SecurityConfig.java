package br.net.yurinogueira.springsales.config;

import br.net.yurinogueira.springsales.domain.service.impl.UserServiceImpl;
import br.net.yurinogueira.springsales.security.jwt.JWTAuthFilter;
import br.net.yurinogueira.springsales.security.jwt.JWTService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserServiceImpl userService;
    private final JWTService jwtService;

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JWTAuthFilter(jwtService, userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         return http
            .csrf().disable()
            .cors().configurationSource(request-> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(List.of("GET", "PATCH", "PUT", "POST"));
                configuration.setAllowedHeaders(List.of("*"));
                return configuration;
            }).and()
            .authorizeRequests()
                .antMatchers("/api/client/**")
                    .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/product/**")
                    .hasAnyRole("ADMIN")
                .antMatchers("/api/cart/**")
                    .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/user/**")
                    .permitAll()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic().and().build();
    }

}
