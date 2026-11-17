package com.softwareprojekt.gruppe8.security;

import static com.vaadin.flow.spring.security.VaadinSecurityConfigurer.vaadin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.softwareprojekt.gruppe8.views.login.LoginView;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain vaadinSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/images/*.png", "/*.css").permitAll());
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/icons/**").permitAll());
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/styles/**").permitAll());

        // Icons from the line-awesome addon
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/line-awesome/**").permitAll());

        http.with(vaadin(), vaadin -> {
            vaadin.loginView(LoginView.class);
        });

        return http.build();
    }

}
