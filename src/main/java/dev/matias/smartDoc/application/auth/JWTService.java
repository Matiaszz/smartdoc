package dev.matias.smartDoc.application.auth;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.matias.smartDoc.Domain.User.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class JWTService {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user){
        if (secret == null || secret.isEmpty()){
            throw new RuntimeException("Secret is not set! Check the environment variables.");
        }

        try{
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            int threeDaysInSeconds = 259200;
            String issuer = "SmartDoc API";

            return JWT.create()
                    .withSubject(user.getId().toString())
                    .withExpiresAt(Instant.now().plusSeconds(threeDaysInSeconds))
                    .withIssuer(issuer)
                    .withClaim("name", user.getName())
                    .withClaim("username", user.getUsername())
                    .withClaim("email", user.getEmail())
                    .withClaim("role", user.getRole().name())
                    .sign(algorithm);

        }catch (Exception e) {
            log.error("Error generating token: {}", e.getMessage());
            return null;
        }
    }

    public String extractSubject(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

}
