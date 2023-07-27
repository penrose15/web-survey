package com.survey.global.security.config;

import com.survey.domain.user.repository.UserRepository;
import com.survey.global.security.filter.JwtAuthenticationFilter;
import com.survey.global.security.filter.JwtVerificationFilter;
import com.survey.global.security.handler.UsersAuthenticationFailureHandler;
import com.survey.global.security.handler.UsersAuthenticationSuccessHandler;
import com.survey.global.security.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    //deprecated 된 부분 수정

    private final UserDetailsService userService;
    private final JwtTokenizer jwtTokenizer;
    private final UserRepository userRepository;
    private final CorsFilter corsFilter;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean //deprecated 된 부분 수정
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        http.apply(new CustomDsl());
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(formLogin -> formLogin.loginPage("/login")
                .defaultSuccessUrl("/articles")
                .failureUrl("/login?error=true"))
                .logout(logout -> logout.logoutSuccessUrl("/login")
                .invalidateHttpSession(true));
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/articles","/login", "/signup", "/user","/api/login").permitAll()
                .anyRequest().authenticated());

        return http.build();
    }

    public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) {

            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter
                    = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer, userRepository);
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new UsersAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new UsersAuthenticationFailureHandler());

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, userRepository);

            builder
                    .addFilter(corsFilter)
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
