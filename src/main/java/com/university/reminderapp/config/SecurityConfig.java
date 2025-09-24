////package com.university.reminderapp.config;
////
////import com.university.reminderapp.security.JwtAuthenticationEntryPoint;
////import com.university.reminderapp.security.JwtAuthenticationFilter;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.authentication.AuthenticationManager;
////import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
////import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.config.http.SessionCreationPolicy;
////import org.springframework.security.crypto.password.NoOpPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.security.web.SecurityFilterChain;
////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
////
////@Configuration
////@EnableWebSecurity
////@EnableMethodSecurity
////public class SecurityConfig {
////    @Autowired
////    private JwtAuthenticationEntryPoint unauthorizedHandler;
////
////    @Bean
////    public JwtAuthenticationFilter jwtAuthenticationFilter() {
////        return new JwtAuthenticationFilter();
////    }
////
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        // In a real application, use BCryptPasswordEncoder
////        return NoOpPasswordEncoder.getInstance();
////    }
////
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
////        return authenticationConfiguration.getAuthenticationManager();
////    }
////
//////    @Bean
//////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//////        http
//////                .cors().and().csrf().disable()
//////                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//////                .authorizeHttpRequests()
//////                .requestMatchers("/", "/auth/**", "/js/**", "/css/**", "/images/**").permitAll()
//////                .requestMatchers("/api/auth/**").permitAll()
//////                .anyRequest().authenticated();
//////
//////        // Add our JWT token filter
//////        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//////
//////        return http.build();
//////    }
////
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////            .csrf(csrf -> csrf.disable())
////            .cors(cors -> cors.configure(http))
////            .exceptionHandling(exceptionHandling ->
////                exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
////            )
////            .sessionManagement(session ->
////                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////            )
////            .authorizeHttpRequests(auth -> auth
////                .requestMatchers("/", "/auth/**", "/js/**", "/css/**", "/images/**").permitAll()
////                .requestMatchers("/api/auth/**").permitAll()
////                .anyRequest().authenticated()
////            );
////
////        // Add our JWT token filter
////        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
////
////        return http.build();
////    }
////}
////
////
////package com.university.reminderapp.config;
////
////import com.university.reminderapp.security.JwtAuthenticationEntryPoint;
////import com.university.reminderapp.security.JwtAuthenticationFilter;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.authentication.AuthenticationManager;
////import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
////import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.config.http.SessionCreationPolicy;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.security.web.SecurityFilterChain;
////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
////
////@Configuration
////@EnableWebSecurity
////@EnableMethodSecurity
////public class SecurityConfig {
////    @Autowired
////    private JwtAuthenticationEntryPoint unauthorizedHandler;
////
////    @Bean
////    public JwtAuthenticationFilter jwtAuthenticationFilter() {
////        return new JwtAuthenticationFilter();
////    }
////
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        // Using BCryptPasswordEncoder for security in production
////        // For development/testing you can still use NoOpPasswordEncoder if needed
////        return new BCryptPasswordEncoder();
////        // return NoOpPasswordEncoder.getInstance(); // Uncomment for development only
////    }
////
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
////        return authenticationConfiguration.getAuthenticationManager();
////    }
////
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf(csrf -> csrf.disable())
////                .cors(cors -> cors.configure(http))
////                .exceptionHandling(exceptionHandling ->
////                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
////                )
////                .sessionManagement(session ->
////                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                )
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/", "/auth/**", "/js/**", "/css/**", "/images/**").permitAll()
////                        .requestMatchers("/api/auth/**").permitAll()
////                        .anyRequest().authenticated()
////                );
////
////        // Add our JWT token filter
////        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
////
////        return http.build();
////    }
////}
//
//
//
//
//package com.university.reminderapp.config;
//
//import com.university.reminderapp.security.JwtAuthenticationEntryPoint;
//import com.university.reminderapp.security.JwtAuthenticationFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
//public class SecurityConfig {
//    @Autowired
//    private JwtAuthenticationEntryPoint unauthorizedHandler;
//
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    @Order(1)
//    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/api/**")
//                .csrf(csrf -> csrf.disable())
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
//                )
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    @Order(2)
//    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/login", "/auth/jwt-login", "/js/**", "/css/**", "/images/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("/login?logout")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//}






package com.university.reminderapp.config;

import com.university.reminderapp.security.JwtAuthenticationEntryPoint;
import com.university.reminderapp.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // WARNING: This is for development ONLY. It stores passwords in plain text.
        // Do NOT use this in a production environment.
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/auth/jwt-login", "/js/**", "/css/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}