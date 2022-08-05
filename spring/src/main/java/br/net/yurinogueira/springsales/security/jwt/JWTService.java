package br.net.yurinogueira.springsales.security.jwt;

import br.net.yurinogueira.springsales.domain.entity.SystemUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTService {

    @Value("${spring.security.jwt.expiration}")
    private String expiration;

    @Value("${spring.security.jwt.signature-key}")
    private String signatureKey;

    public String encodeToken(SystemUser user) {
        long exp = Long.parseLong(expiration);
        LocalDateTime expDate = LocalDateTime.now().plusMinutes(exp);
        Date date = Date.from(expDate.atZone(ZoneId.systemDefault()).toInstant());
        Key key = Keys.hmacShaKeyFor(signatureKey.getBytes(StandardCharsets.UTF_8));

        return Jwts
                .builder()
                .setSubject(user.getLogin())
                .setExpiration(date).signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = decodeToken(token);
            Date expDate = claims.getExpiration();
            LocalDateTime date = expDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(date);
        } catch (Exception exception) {
            return false;
        }
    }

    public String getSystemUserLogin(String token) throws ExpiredJwtException {
        return decodeToken(token).getSubject();
    }

    private Claims decodeToken(String token) throws ExpiredJwtException {
        Key key = Keys.hmacShaKeyFor(signatureKey.getBytes(StandardCharsets.UTF_8));

        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
