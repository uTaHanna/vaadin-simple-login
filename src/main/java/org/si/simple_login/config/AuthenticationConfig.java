package org.si.simple_login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class configures Spring beans for authentication. The implementation is due to the
 * following sources:
 *
 * eparvan (https://stackoverflow.com/questions/42562893/could-not-autowire-no-beans-of-userdetailservice-type-found)
 * Roland Ewald (https://stackoverflow.com/questions/35912404/spring-boot-security-with-vaadin-login)
 * Eugen Paraschiv (http://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)
 * Spring Documentation (https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html)
 *
 */
@Configuration
@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){

        return super.userDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder){

        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * Paths are not secured for now as there is only one page, namely login.
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll();
    }
}
