package com.commigo.metaclass.webconfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Token utility. */
@Component
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenUtil {

  private Key secretKey;

  @Value("${jwt.expiration}000")
  private Long expiration;

  /** Inizializza il controllore dei token. */
  @PostConstruct
  public void init() {
    // Inizializza la chiave segreta al momento della creazione del bean
    secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
  }

  /**
   * genera il token per utente con metaId.
   *
   * @param metaId metaId
   */
  public String generateToken(String metaId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .setSubject(metaId)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  /**
   * Ottieni il metaId dal token.
   *
   * @param token token su cui operare
   * @return metaId
   */
  public String getmetaIdFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

    return claims.getSubject();
  }

  /**
   * Convalida un token.
   *
   * @param token token da convalidare
   * @return ritorna se è valido o meno
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
