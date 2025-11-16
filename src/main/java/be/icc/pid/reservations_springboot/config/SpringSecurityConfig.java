package be.icc.pid.reservations_springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import be.icc.pid.reservations_springboot.service.CustomUserDetailsService;

@Configuration

@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        // Bean pour encoder les mots de passe
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // AuthenticationManager pour utiliser CustomUserDetailsService
        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http,
                        BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
                AuthenticationManagerBuilder authMngrBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
                authMngrBuilder.userDetailsService(customUserDetailsService)
                                .passwordEncoder(bCryptPasswordEncoder);
                return authMngrBuilder.build();
        }

        // Configuration de la sécurité HTTP
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable()) // désactivation CSRF si nécessaire pour tests
                                .authorizeHttpRequests(auth -> {
                                        auth.requestMatchers("/admin").hasRole("ADMIN");
                                        auth.requestMatchers("/user").hasRole("MEMBER");
                                        auth.anyRequest().permitAll();
                                })
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .usernameParameter("login")
                                                .passwordParameter("password")
                                                .failureUrl("/login?loginError=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logoutSuccess=true")
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(
                                                                "/login?loginRequired=true")))
                                .build();
        }
}
