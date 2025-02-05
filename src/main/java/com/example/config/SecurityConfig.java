package com.example.config;

import com.example.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 启用 Spring Security 的 Web 安全功能
@EnableMethodSecurity // 启用方法级别的安全注解
public class SecurityConfig {

  /** 隐含知识点：在 Spring 中， @Bean 方法的参数会被 Spring 容器自动注入，这实际上就是一种构造器注入的形式。 */
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
    http.csrf(csrf -> csrf.disable()) // - 禁用 CSRF（跨站请求伪造）保护,因为是 JWT 认证的 REST API，不需要 CSRF 保护
        .sessionManagement(
            session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS)) // 基于token的认证，不需要启用 session
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/auth/**") // 用户登录接口、刷新token接口所在Controller都以它为开头
                    .permitAll()
                    .anyRequest()
                    .authenticated()) // 这些接口允许匿名访问，其它的接口就需要携带token才能访问
        .addFilterBefore(
            jwtAuthFilter,
            UsernamePasswordAuthenticationFilter
                .class) // 在UsernamePasswordAuthenticationFilter之前执行jwt认证过滤器
    ;

    return http.build();
  }
}
