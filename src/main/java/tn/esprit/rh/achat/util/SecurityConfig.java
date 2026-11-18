package tn.esprit.rh.achat.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Utilisateurs avec mots de passe hachés BCrypt pour les rôles ADMIN et OPERATEUR
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .and()
                .withUser("operateur")
                .password(passwordEncoder().encode("operateur123"))
                .roles("OPERATEUR");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Documentation Swagger / OpenAPI accessible publiquement
                .antMatchers(
                    "/v2/api-docs",
                    "/swagger-resources/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/webjars/**"
                ).permitAll()
                // Actions sensibles réservées à l'administrateur
                .antMatchers(
                    "/operateur/remove-operateur/**",
                    "/facture/remove-facture/**",
                    "/stock/remove-stock/**",
                    "/fournisseur/remove-fournisseur/**",
                    "/categorieProduit/remove-categorieProduit/**",
                    "/secteurActivite/remove-secteurActivite/**"
                ).hasRole("ADMIN")
                // Tous les autres endpoints requièrent d'être authentifié (ADMIN ou OPERATEUR)
                .anyRequest().authenticated()
            .and()
            .httpBasic();
    }
}

