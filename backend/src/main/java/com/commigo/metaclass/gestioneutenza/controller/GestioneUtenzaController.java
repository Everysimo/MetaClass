package com.commigo.metaclass.gestioneutenza.controller;

import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.gestioneutenza.service.GestioneUtenzaService;
import com.commigo.metaclass.utility.MapValidator;
import com.commigo.metaclass.utility.request.RequestUtils;
import com.commigo.metaclass.utility.response.types.LoginResponse;
import com.commigo.metaclass.utility.response.types.Response;
import com.commigo.metaclass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.webconfig.ValidationToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe controller che gestisce tutte le richieste relative all'utente.
 *
 * @author Michele Pesce, Salvatore Alberti, Vincenzo Cutolo
 * @version 1.0
 */
@RestController
public class GestioneUtenzaController {

  @Autowired private GestioneUtenzaService utenzaService;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Autowired private ValidationToken validationToken;

  /**
   * Metodo che gestisce la richeista di login/registrazione da parte di un utente.
   *
   * @param u utente che effettua il login/registrazione
   * @param response utilizzata per gestire i cookie gestendo un token
   * @param result contiene tutti i messaggi di errore contenuti nella richiesta
   * @return body response
   */
  @PostMapping(value = "/login")
  public ResponseEntity<LoginResponse<Boolean>> login(
      @RequestBody Utente u, HttpServletResponse response, BindingResult result) {

    try {

      // Generazione del token JWT usando metaId come identificatore
      String token = jwtTokenUtil.generateToken(u.getMetaId());
      u.setTokenAuth(token);

      // controllo errori di validazione
      if (result.hasErrors()) {
        throw new RuntimeException403(RequestUtils.errorsRequest(result));
      }

      // Aggiungi il token al cookie
      Cookie cookie = new Cookie("jwtToken", token);
      cookie.setPath("/");
      response.addCookie(cookie);

      if (!utenzaService.loginMeta(u)) {
        throw new ServerRuntimeException("errore nel login");
      }

      return ResponseEntity.ok(
          new LoginResponse<>(true, "Login effettuato con successo", token, u.isAdmin()));
    } catch (ServerRuntimeException e) {
      return ResponseEntity.status(500)
          .body(new LoginResponse<>(false, e.getMessage(), null, false));
    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403)
          .body(new LoginResponse<>(false, e.getMessage(), null, false));
    }
  }

  /**
   * Metodo che effettua il logout.
   *
   * @param request richiesta HTTP fornita dal client
   * @param response utilizzata per gestire i cookie gestendo un token
   * @return body response
   */
  @PostMapping("/Manuallogout")
  public ResponseEntity<Response<Boolean>> logout(
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      // Invalida il token lato client rimuovendo il cookie
      Cookie[] cookies = request.getCookies();

      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if ("jwtToken".equals(cookie.getName())) {
            cookie.setMaxAge(0); // Imposta la durata del cookie a 0 secondi per invalidarlo
            response.addCookie(cookie);
          }
        }
      }

      // Ottieni l'header Authorization dalla richiesta
      String authorizationHeader = request.getHeader("Authorization");
      String token = null;

      // Verifica che l'header Authorization sia presente e inizia con "Bearer "
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        // Estrai solo la parte del token dopo "Bearer "
        token = authorizationHeader.substring(7);
      } else {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
      // Rimuovi il token nel sistema
      if (utenzaService.logoutMeta(metaId, validationToken)) {
        return ResponseEntity.ok(new Response<Boolean>(true, "Utente disconnesso con successo"));
      } else {
        throw new ServerRuntimeException("Errore nella rimozione del token dell'utente");
      }
    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(false, se.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(new Response<>(false, "errore"));
    }
  }

  /**
   * Metodo che consente la modifica dei dati dell'utente.
   *
   * @param params nuovi dati dell'utente
   * @param request richiesta HTTP fornita dal client
   * @return body response
   */
  @PostMapping(value = "/modifyUserData")
  public ResponseEntity<Response<Boolean>> modifyUserData(
      @RequestBody Map<String, Object> params, HttpServletRequest request) {
    try {

      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      // Validazione dati utente
      MapValidator.utenteValidate(params);

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      utenzaService.modificaDatiUtente(metaId, params);
      return ResponseEntity.ok(new Response<>(true, "Utente modificato con successo"));

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(null, e.getMessage()));
    }
  }

  /**
   * Metodo che consente di gestire la richiesta di visualizzazione di tutte le stanze dell'utente.
   *
   * @param request richiesta HTTP fornita dal client
   * @return body response
   */
  @GetMapping(value = "/visualizzaStanze")
  public ResponseEntity<Response<List<Stanza>>> visualizzaStanze(HttpServletRequest request) {
    try {
      // Validazione del token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
      List<Stanza> stanze = utenzaService.getStanzeByUserId(metaId);

      if (stanze.isEmpty()) {
        // Se non ci sono stanze, restituisci una risposta con un messaggio appropriato
        return ResponseEntity.ok(new Response<>(stanze, "Non hai accesso a nessuna stanza"));
      }

      // Se ci sono stanze, restituisci una risposta con le stanze e un messaggio di successo
      return ResponseEntity.ok(new Response<>(stanze, "Operazione effettuata con successo"));
    } catch (ServerRuntimeException | RuntimeException403 e) {

      // Gestisci le eccezioni e restituisci una risposta appropriata
      int statusCode = (e instanceof ServerRuntimeException) ? 500 : 403;
      return ResponseEntity.status(statusCode)
          .body(new Response<>(null, "Errore durante l'operazione: " + e.getMessage()));
    }
  }

  /**
   * Metodo che consente di gestire la richiesta di visualizzazione dei dati utente dell'utente.
   *
   * @param request richiesta HTTP fornita dal client
   * @return body response
   */
  @GetMapping(value = "/userDetails")
  public ResponseEntity<Response<Utente>> visualizzaDatiUtente(HttpServletRequest request) {
    Utente utente;
    try {

      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String idMeta = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
      utente = utenzaService.getUtenteByUserId(idMeta);
      if (utente == null) {
        return ResponseEntity.status(500)
            .body(new Response<>(null, "Errore la ricerca dell'utente"));
      } else {
        return ResponseEntity.ok(new Response<>(utente, "operazione effettuata con successo"));
      }
    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(null, e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }
}
