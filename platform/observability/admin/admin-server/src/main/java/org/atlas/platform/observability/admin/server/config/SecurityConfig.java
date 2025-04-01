package org.atlas.platform.observability.admin.server.config;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults())
            .csrf(csrf -> csrf.ignoringRequestMatchers(
                new AntPathRequestMatcher("/instances")
            ))
            .build();
    }
}
