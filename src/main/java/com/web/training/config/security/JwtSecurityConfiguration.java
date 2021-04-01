package com.web.training.config.security;

import com.web.training.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.web.training.config.appConstants.AppConstants.SIGN_UP_URL;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final UserRepository userRepository;

    public JwtSecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests(authorizeRequests -> {
                            authorizeRequests
                                    .antMatchers("/admins/**").hasRole("ADMIN")
                                    .antMatchers("/trainers/getSearchSuggestions").hasRole("USER")
                                    .antMatchers("/trainers/username/**").hasRole("USER")
                                    .antMatchers("/trainers/**").hasRole("TRAINER")
                                    .antMatchers("/users/add").anonymous()
                                    .antMatchers("/users/checkUsername/*").anonymous()
                                    .antMatchers("/users/checkEmail/*").anonymous()
                                    .antMatchers("/users/*/enable").anonymous()
                                    .antMatchers(HttpMethod.POST, SIGN_UP_URL).anonymous()
                                    .anyRequest().authenticated();
                        }
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(new Http401UnauthorizedEntryPoint());
        http.cors();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
