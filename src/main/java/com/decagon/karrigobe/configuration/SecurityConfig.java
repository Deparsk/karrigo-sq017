package com.decagon.karrigobe.configuration;


import com.decagon.karrigobe.security.JWTAuthenticationFilter;
import com.decagon.karrigobe.security.JwtAuthEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    private final JwtAuthEntryPoint authEntryPoint;
    public SecurityConfig(JwtAuthEntryPoint authEntryPoint) {
        this.authEntryPoint = authEntryPoint;
    }
//    @Bean
//    public WebSecurityCustomizer ignoringCustomizer() {
//        return (web) -> web.
//                ignoring()
//                .requestMatchers(antMatcher("/api/v1/users/login"))
//                .requestMatchers(antMatcher("/api/v1/users/signup"))
//                .requestMatchers(antMatcher("/api/v1/reset_password/**"))
//                .requestMatchers(antMatcher("/api/v1/users/verify_email/**")).anyRequest();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.cors(Customizer.withDefaults());
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(configure ->
                configure
                        .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(antMatcher("/javainuse-openapi/**")).permitAll()
                        .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(antMatcher("/javainuse-openapi/**")).permitAll()
                        // Super admin Authorizations
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/users/signup")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/users/login")).permitAll()
                        // Admin
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/admins/signup")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/admins/login")).permitAll()

                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/drivers/login")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/drivers/signup")).hasAnyAuthority("ADMIN")
                        // Driver Task
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/drivers/task_status")).hasAnyAuthority("DRIVER")

                        .requestMatchers(antMatcher("/ws/**")).permitAll()
                        .requestMatchers(antMatcher("/app/**")).permitAll()
                        .requestMatchers(antMatcher("/topic/**")).permitAll()
                        .requestMatchers(antMatcher("/user/**")).permitAll()
                        .requestMatchers(antMatcher("/chatroom/**")).permitAll()
                        .requestMatchers(antMatcher("/message/**")).permitAll()
                        .requestMatchers(antMatcher("/private-message/**")).permitAll()
                        .requestMatchers(antMatcher("/private/**")).permitAll()

                        // Admin Authorizations
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/another_thing/**")).hasAnyAuthority("ADMIN")
                        // DRIVER Authorisation
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/drivers/login")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/drivers/signup")).hasAnyAuthority("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/admins/**")).hasAnyAuthority("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/admins/**")).hasAnyAuthority("ADMIN")
                        // CUSTOMER Authorisation
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/orders/create")).hasAnyAuthority("USER")
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/orders/cancel")).hasAnyAuthority("USER")
                        // Driver Task
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/drivers/task_status")).hasAnyAuthority("DRIVER")
                        .anyRequest().authenticated());
        http.httpBasic(HttpBasicConfigurer::disable);
        http.csrf(CsrfConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(){
        return new JWTAuthenticationFilter();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("POST", "GET", "DELETE", "PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
