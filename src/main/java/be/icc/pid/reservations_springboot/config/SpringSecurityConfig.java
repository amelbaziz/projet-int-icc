package be.icc.pid.reservations_springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/").permitAll();       // Accès libre à la page d'accueil
                    auth.requestMatchers("/admin").hasRole("ADMIN");  // Protégé : admin
                    auth.requestMatchers("/user").hasRole("MEMBER");  // Protégé : user
                    auth.anyRequest().authenticated();          // Le reste nécessite login
                })

                //  FORMULAIRE DE CONNEXION PERSONNALISÉ
                .formLogin(form -> form
                        .loginPage("/login")               // Page HTML personnalisée
                        .usernameParameter("login")        // Nom du champ username
                        .passwordParameter("password")     // Nom du champ password
                        .failureUrl("/login?loginError=true")  // URL en cas d'erreur
                        .permitAll()
                )

                // DÉCONNEXION
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logoutSuccess=true")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                //  GESTION DES EXCEPTIONS
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login?loginRequired=true")
                        )
                )

                //  Optionnel mais demandé : Remember Me
                .rememberMe(Customizer.withDefaults())

                .build();
    }
}
