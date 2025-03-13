//package com.mycompany.teamproject9.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests(auth -> auth
//                        .antMatchers("/", "/home", "/login", "/create", "/signup", "/session/info", "/find-password").permitAll()
//                        .antMatchers("/dashboard/admin").hasRole("ADMIN")
//                        .antMatchers("/dashboard/customer").hasRole("CUSTOMER")
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                        .maximumSessions(1)
//                        .expiredUrl("/login?expired")
//                )
//                .formLogin().disable() // ✅ Spring Security 기본 로그인 비활성화
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/")
//                        .permitAll()
//                )
//                // ✅ 로그인하지 않은 상태에서 보호된 페이지 접근 시 로그인 페이지로 리디렉트
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendRedirect("/login");
//                        })
//                );
//
//        return http.build();
//    }
//}