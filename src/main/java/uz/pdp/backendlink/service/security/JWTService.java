package uz.pdp.backendlink.service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.pdp.backendlink.entity.User;
import uz.pdp.backendlink.repository.UserRepository;

import java.util.Date;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class JWTService {

    @Value("${myapp.secret-key}")
    private String SECRET_KEY;

    private final UserRepository userRepository;

    public String generateToken(String username, Date date) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found for token: " + username));

        return Jwts.builder()
                .subject(username)
                .claim("role", user.getRole().name())
                .claim("randomId", UUID.randomUUID().toString())
                .expiration(date)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();

    }

    public String parseToken(String token) {

        DefaultClaims payload = (DefaultClaims) Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parse(token)
                .getPayload();

        return payload.getSubject();
    }

    public String verifyToken(String refreshToken) {

        DefaultClaims payload = (DefaultClaims) Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parse(refreshToken)
                .getPayload();

        if (payload.getExpiration().before(new Date())) {

            throw new RuntimeException("Token expired");

        }

        return payload.getSubject();
    }

}
