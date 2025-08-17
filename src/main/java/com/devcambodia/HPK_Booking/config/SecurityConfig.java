package com.devcambodia.HPK_Booking.config;

import com.devcambodia.HPK_Booking.security.CustomUserDetail;
import com.devcambodia.HPK_Booking.security.CustomUserDetailService;
import com.devcambodia.HPK_Booking.security.filter.CustomAuthenticationProvider;
import com.devcambodia.HPK_Booking.security.filter.JwtAuthFilter;
import com.devcambodia.HPK_Booking.security.filter.JwtAuthInternalFilter;
import com.devcambodia.HPK_Booking.service.JwtService;
import com.devcambodia.HPK_Booking.utils.CustomResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final JwtConfig jwtConfig;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final JwtService  jwtService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeRequests()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/file/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/authorization").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/api/v1/user/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/booking/**").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and().
                authenticationManager(authenticationManager)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\": \"Unauthorized\"}");
                                })
                       .authenticationEntryPoint(((((request, response, authException) ->
                               response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))))
                                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\": \"Forbidden\"}");
                                }))
                ).addFilterBefore(new JwtAuthFilter(jwtService,customUserDetailService,jwtConfig,authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthInternalFilter(jwtService,jwtConfig), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
