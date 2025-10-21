////////package com.login_project.Config;
////////
////////import com.login_project.Service.CustomUserDetailsService;
////////import org.springframework.context.annotation.Bean;
////////import org.springframework.context.annotation.Configuration;
////////import org.springframework.http.HttpMethod;
////////import org.springframework.security.authentication.AuthenticationManager;
////////import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
////////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////////import org.springframework.security.config.http.SessionCreationPolicy;
////////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////////import org.springframework.security.crypto.password.PasswordEncoder;
////////import org.springframework.security.web.SecurityFilterChain;
////////import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
////////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
////////
////////@Configuration
////////@EnableWebSecurity
////////public class SecurityConfig {
////////
////////    // --- Use constructor injection instead of @Autowired fields ---
////////    private final CustomUserDetailsService customUserDetailsService;
////////    private final JwtRequestFilter jwtRequestFilter;
////////
////////    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
////////        this.customUserDetailsService = customUserDetailsService;
////////        this.jwtRequestFilter = jwtRequestFilter;
////////    }
////////    // -----------------------------------------------------------
////////
////////    @Bean
////////    public PasswordEncoder passwordEncoder() {
////////        return new BCryptPasswordEncoder();
////////    }
////////
////////    @Bean
////////    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
////////        return config.getAuthenticationManager();
////////    }
////////
////////    @Bean
////////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////////        http
////////                .csrf(csrf -> csrf.disable())
////////                .authorizeHttpRequests(auth -> auth
////////                        .requestMatchers("/home", "/login", "/register", "/api/login", "/api/register").permitAll()
////////                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
////////                        .requestMatchers(HttpMethod.GET, "/api/events").hasAnyRole("USER", "ADMIN")
////////                        .requestMatchers(HttpMethod.POST, "/api/events/**/apply").hasRole("USER")
////////                        .anyRequest().authenticated()
////////                )
////////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////////                // Use the injected jwtRequestFilter bean here
////////                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
////////                .exceptionHandling(ex -> ex
////////                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
////////                );
////////
////////        return http.build();
////////    }
////////}
//////
//////package com.login_project.Config;
//////
//////import org.springframework.beans.factory.annotation.Autowired;
//////import org.springframework.context.annotation.Bean;
//////import org.springframework.context.annotation.Configuration;
//////import org.springframework.http.HttpMethod;
//////import org.springframework.security.authentication.AuthenticationManager;
//////import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//////import org.springframework.security.config.http.SessionCreationPolicy;
//////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//////import org.springframework.security.crypto.password.PasswordEncoder;
//////import org.springframework.security.web.SecurityFilterChain;
//////import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//////
//////@Configuration
//////@EnableWebSecurity
//////public class SecurityConfig {
//////
//////    // We can now revert to simple field injection as the cycle is broken.
//////    @Autowired
//////    private JwtRequestFilter jwtRequestFilter;
//////
//////    @Bean
//////    public PasswordEncoder passwordEncoder() {
//////        return new BCryptPasswordEncoder();
//////    }
//////
//////    @Bean
//////    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//////        return config.getAuthenticationManager();
//////    }
//////
//////    @Bean
//////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//////        http
//////                .csrf(csrf -> csrf.disable())
//////                .authorizeHttpRequests(auth -> auth
//////                        // Publicly accessible URLs
//////                        .requestMatchers("/home", "/login", "/register", "/api/login", "/api/register").permitAll()
//////
//////                        // Admin-only URLs
//////                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//////
//////                        // URLs accessible by both users and admins
//////                        .requestMatchers(HttpMethod.GET, "/api/events").hasAnyRole("USER", "ADMIN")
//////
//////                        // --- THIS IS THE FIX ---
//////                        // User-only URLs: Changed the invalid pattern from ** to *
//////                        .requestMatchers(HttpMethod.POST, "/api/events/*/apply").hasRole("USER")
//////                        // ------------------------
//////
//////                        // All other requests must be authenticated
//////                        .anyRequest().authenticated()
//////                )
//////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//////                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//////                .exceptionHandling(ex -> ex
//////                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//////                );
//////
//////        return http.build();
//////    }
//////}
////package com.login_project.Config;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.http.HttpMethod;
////import org.springframework.security.authentication.AuthenticationManager;
////import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.config.http.SessionCreationPolicy;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.security.web.SecurityFilterChain;
////import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
////import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
////import org.springframework.security.web.util.matcher.RequestMatcher;
////import org.springframework.web.servlet.config.annotation.EnableWebMvc;
////
////import java.util.LinkedHashMap;
////
////@Configuration
////@EnableWebSecurity
////@EnableWebMvc
////public class SecurityConfig {
////
////    @Autowired
////    private JwtRequestFilter jwtRequestFilter;
////
////    // Inject the custom entry point for API errors
////    @Autowired
////    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
////
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
////        return config.getAuthenticationManager();
////    }
////
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf(csrf -> csrf.disable())
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/home", "/login", "/register", "/api/login", "/api/register").permitAll()
////                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
////                        .requestMatchers(HttpMethod.GET, "/api/events").hasAnyRole("USER", "ADMIN")
////                        .requestMatchers(HttpMethod.POST, "/api/events/*/apply").hasRole("USER")
////                        .anyRequest().authenticated()
////                )
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
////
////                // --- THIS IS THE UPDATED EXCEPTION HANDLING LOGIC ---
////                .exceptionHandling(ex -> {
////                    // Define the entry point for browser-based navigation (redirect to /login)
////                    LoginUrlAuthenticationEntryPoint browserEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");
////
////                    // Create a map to hold different entry points based on the request type
////                    LinkedHashMap<RequestMatcher, org.springframework.security.web.AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();
////
////                    // For any request that starts with /api/, use the JWT entry point (sends 401)
////                    entryPoints.put(new AntPathRequestMatcher("/api/**"), jwtAuthenticationEntryPoint);
////
////                    // For all other requests, use the browser entry point (redirects)
////                    org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint delegatingEntryPoint =
////                            new org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint(entryPoints);
////                    delegatingEntryPoint.setDefaultEntryPoint(browserEntryPoint);
////
////                    ex.authenticationEntryPoint(delegatingEntryPoint);
////                });
////        // ----------------------------------------------------
////
////        return http.build();
////    }
////}
////
////
//
//package com.login_project.Config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc; // <-- NEW IMPORT
//
//import java.util.LinkedHashMap;
//
//@Configuration
//@EnableWebSecurity
//@EnableWebMvc // <-- ADD THIS ANNOTATION
//public class SecurityConfig {
//
//    @Autowired
//    private JwtRequestFilter jwtRequestFilter;
//
//    @Autowired
//    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/home", "/login", "/register", "/api/login", "/api/register").permitAll()
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        // Allow unauthenticated access to event images. Browsers fetch these independently.
//                        .requestMatchers(HttpMethod.GET, "/api/events/*/image").permitAll() // <-- IMPORTANT: Add this rule
//                        .requestMatchers(HttpMethod.GET, "/api/events").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/api/events/*/apply").hasRole("USER")
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(ex -> {
//                    LoginUrlAuthenticationEntryPoint browserEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");
//                    LinkedHashMap<RequestMatcher, org.springframework.security.web.AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();
//                    entryPoints.put(new AntPathRequestMatcher("/api/**"), jwtAuthenticationEntryPoint);
//                    org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint delegatingEntryPoint =
//                            new org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint(entryPoints);
//                    delegatingEntryPoint.setDefaultEntryPoint(browserEntryPoint);
//                    ex.authenticationEntryPoint(delegatingEntryPoint);
//                });
//
//        return http.build();
//    }
//}
//
//

package com.login_project.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.LinkedHashMap;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        // --- THIS IS THE MODERN WAY TO CREATE A MATCHER ---
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        // -------------------------------------------------

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home", "/login", "/register", "/api/login", "/api/register").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/events/*/image").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/events/*/apply").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> {
                    LoginUrlAuthenticationEntryPoint browserEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");

                    LinkedHashMap<RequestMatcher, org.springframework.security.web.AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();

                    // --- USE THE NEW MVC MATCHER HERE ---
                    entryPoints.put(mvcMatcherBuilder.pattern("/api/**"), jwtAuthenticationEntryPoint);
                    // ------------------------------------

                    org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint delegatingEntryPoint =
                            new org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint(entryPoints);
                    delegatingEntryPoint.setDefaultEntryPoint(browserEntryPoint);
                    ex.authenticationEntryPoint(delegatingEntryPoint);
                });

        return http.build();
    }
}

