package com.survey.global.security.filter;

import com.survey.domain.user.entity.User;
import com.survey.domain.user.repository.UserRepository;
import com.survey.global.security.jwt.JwtTokenizer;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);
        }  catch (ExpiredJwtException ee) {
            ee.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request,response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");
        return authorization == null || !authorization.startsWith("Bearer ");
    }

    private String getAccessJwtToken(HttpServletRequest request) {
        if(request.getHeader("Authorization") != null) {
            return request.getHeader("Authorization").substring(7);
        }
        return null;
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");

        return jwtTokenizer.getClaims(jws).getBody();
    }
    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 멤버"));

        List<GrantedAuthority> authorities = new ArrayList<>();
        String roles = (String) claims.get("roles");
        authorities.add(new SimpleGrantedAuthority("ROLE_" + roles));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
