package dev.omedia.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests(
                        (request) ->
                            request
                                    .antMatchers("/").permitAll()
                                    .anyRequest().authenticated()
                )
                .formLogin();
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("CREATE_ACT_ROLE", "AMEND_ACT_ROLE", "EDIT_ACT_ROLE", "PUBLIC_ROLE", "DELETE_ACT_ROLE")
                .build();

        UserDetails regularUser = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("PUBLIC_ROLE")
                .build();

        return new InMemoryUserDetailsManager(admin, regularUser);
    }
}
