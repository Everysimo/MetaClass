package com.commigo.metaclass.gestioneamministrazione.controller;

import com.commigo.metaclass.entity.Categoria;
import com.commigo.metaclass.entity.Scenario;
import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.gestioneamministrazione.service.GestioneAmministrazioneService;
import com.commigo.metaclass.gestionestanza.controller.GestioneStanzaControl;
import com.commigo.metaclass.utility.request.RequestUtils;
import com.commigo.metaclass.utility.response.types.Response;
import com.commigo.metaclass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.webconfig.ValidationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Classe per le API della gestione amministrazione. */
@RestController
@RequestMapping("/admin")
public class GestioneAmministrazioneController {

  @Autowired private GestioneAmministrazioneService gestioneamministrazione;

  @Autowired private ValidationToken validationToken;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Autowired private GestioneStanzaControl stanzaControl;

  /**
   * confronta il metaId di un utente con quelli degli admin, per verificare se l'utente è un admin.
   *
   * @param metaId metaId che deve essere confrontato
   */
  public boolean checkAdmin(String metaId) {
    return gestioneamministrazione.checkAdmin(metaId); // chiamata al servizio
  }

  /**
   * Metodo che gestisce la richiesta di fornire la lista degli utenti bannati in una determinata
   * stanza da parte di un admin di sistema.
   *
   * @param id Id della stanza di cui vogliamo visualizzare gli utenti bannati
   * @param request richiesta HTTP fornita dal client
   */
  @PostMapping(value = "/visualizzaUtentiBannatiInStanza/{Id}")
  public ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(
      @PathVariable Long id, HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      if (!checkAdmin(metaId)) {
        throw new RuntimeException403("Non sei amministratore");
      }
      return stanzaControl.visualizzaUtentiBannatiInStanza(id, request);

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
    }
  }

  /**
   * Metodo che gestisce la richiesta di annullamento del ban di un determinato utente in una
   * determinata stanza da parte di un admin di sistema.
   *
   * @param idUtente Id dell'utente a cui deve essere eliminato il ban
   * @param idStanza Id della stanza in cui l'utente è bannato
   * @param request richiesta HTTP fornita dal client
   */
  @PostMapping(value = "annullaBan/{idStanza}/{idUtente}")
  public ResponseEntity<Response<Boolean>> annullaBan(
      @PathVariable Long idUtente,
      @PathVariable("idStanza") Long idStanza,
      HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      // verifica dei permessi
      if (!checkAdmin(metaId)) {
        throw new RuntimeException403("non sei amministratore");
      }

      gestioneamministrazione.deleteBanToUser(idUtente, idStanza);
      return ResponseEntity.ok(new Response<>(true, "Ban annullato correttamente"));

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new Response<>(false, "Errore durante la richiesta: " + e.getMessage()));
    }
  }

  /**
   * Metodo che gestisce la richiesta di creazione di una categoria da parte di un admin di sistema.
   *
   * @param c Categoria che deve essere creata
   * @param result Variabile che contiene tutti gli errori di validazione dell'oggetto categoria
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "updateCategoria")
  public ResponseEntity<Response<Boolean>> updateCategoria(
      @Valid @RequestBody Categoria c, BindingResult result, HttpServletRequest request) {
    try {

      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      // verifica dei permessi
      if (!checkAdmin(metaId)) {
        throw new RuntimeException403("non sei amministratore");
      }

      // controllo errori di validazione
      if (result.hasErrors()) {
        throw new RuntimeException403(RequestUtils.errorsRequest(result));
      }

      if (!gestioneamministrazione.updateCategoria(c)) {
        throw new ServerRuntimeException("Errore durante l'inserimento della categoria");
      } else {
        return ResponseEntity.ok(new Response<>(true, "categoria creata con successo"));
      }
    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
    } catch (ServerRuntimeException e) {
      return ResponseEntity.status(500).body(new Response<>(false, e.getMessage()));
    }
  }

  /**
   * Metodo che gestisce la richiesta di creazione uno scenario da parte di un admin di sistema.
   *
   * @param s Scenario che deve essere creato
   * @param result Variabile che contiene tutti gli errori di validazione dell'oggetto scenario
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "updateScenario")
  public ResponseEntity<Response<Boolean>> updateScenario(
      @Valid @RequestBody Scenario s, BindingResult result, HttpServletRequest request) {
    try {
      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      // verifica dei permessi
      if (!checkAdmin(metaId)) {
        throw new RuntimeException403("accesso non consentito");
      }

      // controllo errori di validazione
      if (result.hasErrors()) {
        throw new RuntimeException403(RequestUtils.errorsRequest(result));
      }

      gestioneamministrazione.updateScenario(s, s.getCategoria().getId());
      return ResponseEntity.ok(new Response<>(true, "scenario creato con successo"));

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di visualizzare tutte le stanza da parte di un adin
   * di sistema.
   *
   * @param request richiesta HTTP fornita dal client
   * @return una lista di stanze ed un messaggio che descrive l'esito dell'operazione
   */
  @GetMapping(value = "allStanze")
  public ResponseEntity<Response<List<Stanza>>> visualizzaStanze(HttpServletRequest request) {
    List<Stanza> stanze;
    try {
      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      // verifica dei permessi
      if (!checkAdmin(metaId)) {
        throw new RuntimeException403("accesso non consentito");
      }

      stanze = gestioneamministrazione.getStanze();
      if (stanze == null) {
        throw new ServerRuntimeException("Errore nella ricerca delle stanze");
      } else if (stanze.isEmpty()) {
        return ResponseEntity.ok(new Response<>(stanze, "nessuna stanza creata"));
      } else {
        return ResponseEntity.ok(new Response<>(stanze, "operazione effettuata con successo"));
      }
    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(null, "Errore durante l'operazione: " + re.getMessage()));
    } catch (ServerRuntimeException e) {
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }

  /**
   * Metodo che permette la gestire la richiesta di visualizzazione di tutte le categorie.
   *
   * @param request richiesta HTTP fornita dal client
   * @return lista di categoria ed un messagggio che identifica l'esito dell'operazione
   */
  @GetMapping(value = "visualizzaCategoria")
  public ResponseEntity<Response<List<Categoria>>> visualizzaCategorie(HttpServletRequest request) {
    List<Categoria> cats;
    try {
      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      // verifica dei permessi
      if (!checkAdmin(metaId)) {
        throw new RuntimeException403("accesso non consentito");
      }

      cats = gestioneamministrazione.getCategorie();
      if (cats == null) {
        throw new ServerRuntimeException("Errore nella ricerca delle categorie");
      } else if (cats.isEmpty()) {
        return ResponseEntity.ok(new Response<>(cats, "nessuna categoria creata"));
      } else {
        return ResponseEntity.ok(new Response<>(cats, "operazione effettuata con successo"));
      }
    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(null, "Errore durante l'operazione: " + re.getMessage()));
    } catch (ServerRuntimeException e) {
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di modifica di una determinata stanza da parte di
   * un admin.
   *
   * @param id ID della stanza che deve essere modificata
   * @param params Nuovi attributi della stanza
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "modificaStanza/{Id}")
  public ResponseEntity<Response<Boolean>> modifyRoomDataAdmin(
      @PathVariable Long id, @RequestBody Map<String, Object> params, HttpServletRequest request) {

    try { // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      // verifica dei permessi
      if (!checkAdmin(metaId)) {
        throw new RuntimeException403("accesso non consentito");
      }

      return stanzaControl.modifyRoomData(id, params, request);
    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(null, "Errore durante l'operazione: " + re.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di eliminazione di una stanza da parte di un admin
   * di sistema.
   *
   * @param id ID della stanza che deve essere eliminata
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "eliminaStanza/{Id}")
  public ResponseEntity<Response<Boolean>> eliminaStanza(
      @PathVariable Long id, HttpServletRequest request) {
    try {
      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      // verifica dei permessi
      if (!checkAdmin(metaId)) {throw new RuntimeException403("accesso non consentito");}

      return stanzaControl.eliminaStanza(id, request);
    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "Errore durante l'operazione: " + re.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di visualizzazione di una stanza da parte di un
   * admin di sistema.
   *
   * @param id ID della stanza che deve essere visualizzata
   * @param request richiesta HTTP fornita dal client
   * @return la stanza che deve essere visualizzata, ed un messaggio che descrive l'esito
   *     dell'operazione
   */
  @PostMapping(value = "visualizzaStanza/{Id}")
  public ResponseEntity<Response<Stanza>> visualizzaStanza(
      @PathVariable Long id, HttpServletRequest request) {
    try {
      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      // verifica dei permessi
      if (!checkAdmin(metaId)) {
        throw new RuntimeException403("Non sei un amministratore");
      }

      return stanzaControl.visualizzaStanza(id, request);
    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(null, "Errore durante l'operazione: " + re.getMessage()));
    }
  }
}
