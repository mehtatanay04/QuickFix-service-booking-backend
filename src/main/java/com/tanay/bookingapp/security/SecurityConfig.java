package com.tanay.bookingapp.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

private final JwtAuthFilter jwtAuthFilter;

public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
this.jwtAuthFilter = jwtAuthFilter;
}

@Bean
public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {

FilterRegistrationBean<JwtAuthFilter> registrationBean =
new FilterRegistrationBean<>();

registrationBean.setFilter(jwtAuthFilter);

registrationBean.addUrlPatterns("/api/*");

return registrationBean;
}
}