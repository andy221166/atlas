package org.atlas.platform.auth.jwt.core;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(
    securedEnabled = true, // Enables @Secured annotation
    jsr250Enabled = true,  // Enables @RolesAllowed annotation
    prePostEnabled = true  // Enables @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter annotations
)
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsService userDetailsService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        // Fix cannot login H2 console due to forbidden error
        .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .authorizeHttpRequests(a -> a.anyRequest().permitAll())
        // No session should be created
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(e -> {
          // Unauthorized
          e.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
          // Access denied
          e.accessDeniedHandler(new CustomAccessDeniedHandler());
        })
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
