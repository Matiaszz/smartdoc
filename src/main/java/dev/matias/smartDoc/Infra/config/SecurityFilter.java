package dev.matias.smartDoc.Infra.config;

import dev.matias.smartDoc.Domain.User.UserRepository;
import dev.matias.smartDoc.application.auth.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Configuration
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverUserLoginToken(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UUID id = UUID.fromString(token);
            UserDetails user = userRepository.findById(id).orElseThrow(
                    () -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User not found (Security Filter)")
            );

            var authentication = new UsernamePasswordAuthenticationToken(user, null);
            log.info("Authenticated user: {}", user.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e){
            log.error("Error on ID casting.");
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String recoverUserLoginToken(HttpServletRequest request) {
        if (request.getCookies() != null){
            for (var cookie : request.getCookies()){
                if ("userToken".equalsIgnoreCase(cookie.getName())){
                    log.info("User Token received from cookie: {}", cookie.getName());
                    return cookie.getValue();
                }
            }
        }
        log.info("User Token in token recovery is null.");
        return null;
    }


}
