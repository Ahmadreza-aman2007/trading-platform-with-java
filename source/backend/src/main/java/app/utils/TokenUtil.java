package app.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenUtil {
    @Value("${token.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secret;
    @Value("${token.expiration:86400000}")
    private Long expiration;
    
    public String generateToken(String username, String role) {
        if(expiration==null){
            expiration= 86400000L;
        }
        if(secret==null){
            secret="404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getExpiration() {
        return expiration;
    }
}
