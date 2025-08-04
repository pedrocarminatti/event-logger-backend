package com.el.eventlogger.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final Key key;
  private final long jwtExpirationInMs;

  public JwtTokenProvider(
      @Value("${app.jwt.secret}") String jwtSecret,
      @Value("${app.jwt.expiration}") long jwtExpirationInMs) {
    this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    this.jwtExpirationInMs = jwtExpirationInMs;
  }

  public String generateToken(String email) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getEmailFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateToken(String token, String username) {
    String email = getEmailFromToken(token);
    return email.equals(username) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    Date expiration = getClaims(token).getExpiration();
    return expiration.before(new Date());
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }
}
