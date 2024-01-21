package com.commigo.metaclass.webconfig;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe per la validazione del token. */
@Component
@Data
public class ValidationToken {

  @Autowired private JwtTokenUtil jwtTokenUtil;
  private String token;

  /**
   * Controlla se il token è valido.
   *
   * @param request richiesta da cui prende il token
   * @return ritorna se è valido o no il token
   */
  public boolean isTokenValid(HttpServletRequest request) {
    this.token = request.getHeader("Authorization");

    if (this.token != null && this.token.startsWith("Bearer ")) {
      this.token = this.token.substring(7);

      return jwtTokenUtil.validateToken(this.token);
    }

    return false;
  }
}
