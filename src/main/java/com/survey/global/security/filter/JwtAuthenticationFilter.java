package com.survey.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.survey.domain.user.entity.User;
import com.survey.domain.user.repository.UserRepository;
import com.survey.global.security.dto.LoginDTO;
import com.survey.global.security.jwt.JwtTokenizer;
import com.survey.global.security.jwt.refreshToken.RefreshToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDTO loginDTO = objectMapper.readValue(request.getInputStream(), LoginDTO.class);

        User user = userRepository.findByEmail(loginDTO.getUsername())
                .orElseThrow(() -> new AuthenticationException("email not found") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                });

        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new AuthenticationException("wrong password") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String accessToken = delegateAccessToken(user);

        response.setHeader("Authorization","Bearer "+ accessToken);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    private String delegateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getEmail());
        claims.put("roles",user.getRole().name());

        String subject = user.getEmail();
        return jwtTokenizer.generateAccessToken(claims, subject);
    }
}
