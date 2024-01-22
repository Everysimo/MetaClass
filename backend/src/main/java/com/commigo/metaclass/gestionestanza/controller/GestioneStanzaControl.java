package com.commigo.metaclass.gestionestanza.controller;

import com.commigo.metaclass.entity.*;
import com.commigo.metaclass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.exceptions.RuntimeException401;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.utility.MapValidator;
import com.commigo.metaclass.utility.request.RequestUtils;
import com.commigo.metaclass.utility.response.ResponseUtils;
import com.commigo.metaclass.utility.response.types.AccessResponse;
import com.commigo.metaclass.utility.response.types.Response;
import com.commigo.metaclass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.webconfig.ValidationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.RestController;

/** Richieste API per la gestione stanza. */
@RestController
public class GestioneStanzaControl {

  @Autowired private GestioneStanzaService stanzaService;

  @Autowired private ValidationToken validationToken;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  /**
   * metodo che gestisce la richiesta di ban di un utente all'interno di una stanza.
   *
   * @param idStanza id della stanza da cui si vuole bannare l'utente
   * @param idUtente id dell'utente da bannare
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/banUtente/{idStanza}/{idUtente}")
  public ResponseEntity<Response<Boolean>> banUtente(
      @PathVariable Long idStanza, @PathVariable Long idUtente, HttpServletRequest request) {

    try {
      // controllo del token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
      return stanzaService.banUtente(idStanza, metaid, idUtente);

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "Errore durante l'operazione: " + re.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(new Response<>(false, "Errore durante l'operazione"));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di creazione di una stanza.
   *
   * @param s Stanza che deve essere creata
   * @param result varaibile che conteine tutti gli errori di validazione della classe Stanza
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/creastanza")
  public ResponseEntity<Response<Boolean>> creaStanza(
      @Valid @RequestBody Stanza s, BindingResult result, HttpServletRequest request) {
    try {
      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      if (result.hasErrors()) {
        throw new RuntimeException403(RequestUtils.errorsRequest(result));
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      stanzaService.creaStanza(s, metaid);
      return ResponseUtils.getResponseOk("Corretto");

    } catch (ServerRuntimeException e) {
      return ResponseUtils.getResponseError(
          HttpStatus.INTERNAL_SERVER_ERROR, "Errore durante la richiesta: " + e.getMessage());
    } catch (RuntimeException403 se) {
      return ResponseUtils.getResponseError(
          HttpStatus.valueOf(403), "Errore durante la richiesta: " + se.getMessage());
    } catch (Exception ge) {
      return ResponseUtils.getResponseError(
          HttpStatus.valueOf(500), "Errore durante la richiesta: " + ge.getMessage());
    }
  }

  /**
   * metodo che permette di gestire la richiesta di downgrade di un organizzatore all'interno di una
   * specifica stanza.
   *
   * @param idStanza id della stanza in cui si vuole effettuare il downgrade dell'organizzatore
   * @param idUtente id dell'organizzatore a cui deve essere effettuato il downgrade
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/declassaOrganizzatore/{idStanza}/{idUtente}")
  public ResponseEntity<Response<Boolean>> declassaOrganizzatore(
      @PathVariable Long idStanza, @PathVariable Long idUtente, HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      return ResponseEntity.ok(stanzaService.downgradeUtente(metaid, idUtente, idStanza));

    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500)
          .body(new Response<>(false, "Errore durante l'operazione: " + se.getMessage()));
    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "Errore durante l'operazione: " + re.getMessage()));
    }
  }

  /**
   * metodo che premette di gestire la richiesta di eliminazione di una specifica stanza.
   *
   * @param id id della stanza che deve essere eliminata
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/eliminaStanza/{id}")
  public ResponseEntity<Response<Boolean>> eliminaStanza(
      @PathVariable Long id, HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      return ResponseEntity.ok(stanzaService.deleteRoom(metaid, id));

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "Errore durante l'operazione: " + re.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(500)
          .body(new Response<>(false, "Errore durante l'operazione" + e.getMessage()));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di gestione dell'accesso di un determinato utente
   * all'interno di una specifica stanza.
   *
   * @param idStanza id della stanza in cui si deve gestire l'accesso dell'utente
   * @param idUtente id dell'utente di cui si deve gestire l'accesso
   * @param scelta variabile che identifica la scelta dell'organizzatore sulla gestione dell'accesso
   *     alla stanza
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/gestioneAccessi/{idStanza}/{idUtente}")
  public ResponseEntity<Response<Boolean>> gestioneAccessi(
      @PathVariable Long idStanza,
      @PathVariable Long idUtente,
      @RequestBody String scelta,
      HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(scelta);
      boolean newScelta = jsonNode.get("scelta").asBoolean();

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
      return stanzaService.gestioneAccesso(metaid, idUtente, idStanza, newScelta);

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "Errore nell'operazione: " + re.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(500)
          .body(new Response<>(false, "Errore durante l'operazione" + e.getMessage()));
    }
  }

  /**
   * metodo che permette di gestire di richiesta di modifica dei dati di una stanza.
   *
   * @param id id della stanza di cui bisogna midificare i dati
   * @param params nuovi dati dellas tanza
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/modifyRoomData/{id}")
  public ResponseEntity<Response<Boolean>> modifyRoomData(
      @PathVariable Long id, @RequestBody Map<String, Object> params, HttpServletRequest request) {

    try {
      // controllo del token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      // validazione della map
      MapValidator.stanzaValidate(params);

      if (!stanzaService.modificaDatiStanza(params, id)) {
        throw new ServerRuntimeException("modifica non effettuata");
      } else {
        return ResponseEntity.ok(new Response<>(true, "Stanza modificata con successo"));
      }

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "Errore durante l'operazione: " + re.getMessage()));
    } catch (RuntimeException401 ue) {
      return ResponseEntity.status(401)
          .body(new Response<>(false, "Errore durante l'operazione: " + ue.getMessage()));
    } catch (ClientRuntimeException ce) {
      return ResponseEntity.status(400).body(new Response<>(false, ce.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(new Response<>(false, "Errore durante l'operazione"));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di promoziione di un utente ad organizzatore
   * all'interno di una specifica stanza.
   *
   * @param idStanza id della stanza in cui si vuole promuovere l'utente
   * @param idUtente id dell'utente che deve essere promosso ad organizzatore
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/promuoviOrganizzatore/{idStanza}/{idUtente}")
  public ResponseEntity<Response<Boolean>> promuoviOrganizzatore(
      @PathVariable Long idStanza, @PathVariable Long idUtente, HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      return ResponseEntity.ok(stanzaService.upgradeUtente(metaid, idUtente, idStanza));

    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500)
          .body(new Response<>(false, "Errore durante l'operazione: " + se.getMessage()));
    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "Errore durante l'operazione: " + re.getMessage()));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di accesso ad una determinata sta da parte di un
   * utente.
   *
   * @param requestBody codice della stanza a cui l'utente vuole fare accesso
   * @param request richiesta HTTP fornita dal client
   * @return un valore intero che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/accessoStanza")
  public ResponseEntity<AccessResponse<Long>> richiestaAccessoStanza(
      @RequestBody String requestBody, HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(requestBody);
      JsonNode codiceNode = jsonNode.get("codice");

      // controllo se il codice è null
      if (codiceNode == null) {
        throw new RuntimeException403(
            "l'attributo deve essere nominato 'codice' e non diversamente");
      }

      // controllo se l'attributo è testuale
      if (!codiceNode.isTextual()) {
        throw new RuntimeException403("l'attributo deve essere una stringa");
      }

      String codiceStanza = codiceNode.asText();
      System.out.println(codiceStanza.length());
      if (codiceStanza.length() != 6) {
        throw new RuntimeException403("il codice deve essere un numero di 6 cifre");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
      return ResponseEntity.ok(stanzaService.accessoStanza(codiceStanza, metaId).getBody());

    } catch (JsonProcessingException je) {
      return ResponseEntity.status(403)
          .body(
              new AccessResponse<>(
                  -1L, "Errore durante la richiesta: il body della tua richiesta è vuoto", false));
    } catch (RuntimeException403 re) {
      return ResponseEntity.status(403)
          .body(
              new AccessResponse<>(-1L, "Errore durante la richiesta: " + re.getMessage(), false));
    } catch (ServerRuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new AccessResponse<>(-1L, "Errore durante la richiesta: " + e.getMessage(), false));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di visualizzazione di tutti gli utenti bannati
   * all'interno di una stanza.
   *
   * @param id id della stanza di cui vogliono visualizzare tutti gli utenti bannati
   * @param request richiesta HTTP fornita dal client
   * @return lista degli utenti bannati ed un messaggio che descrive l'esito dell'operazione
   */
  public ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(
      @PathVariable Long id, HttpServletRequest request) throws RuntimeException403 {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      return stanzaService.visualizzaUtentiBannatiInStanza(id);

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
    }
  }

  /**
   * metoodo che permette di gestire la richiesta di visualizzazione di tutti gli utenti all'interno
   * di una specifica stanza.
   *
   * @param id id della stanza di sui si vogliono visualizzare gli utenti
   * @param request richiesta HTTP fornita dal client
   * @return lista degli utenti presenti nella stanza ed un messaggio che descrive l'esito
   *     dell'operazione
   */
  @PostMapping(value = "/visualizzaUtentiInStanza/{id}")
  public ResponseEntity<Response<List<Utente>>> visualizzaUtentiInStanza(
      @PathVariable Long id, HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      return stanzaService.visualizzaUtentiInStanza(id);

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
    }
  }

  /**
   * metoodo che permette di gestire la richiesta di visualizzazione degli utenti in attesa di una
   * specifica stanza.
   *
   * @param id id della stanza di sui si vogliono visualizzare gli utenti in attesa
   * @param request richiesta HTTP fornita dal client
   * @return lista degli utenti in attesa nella stanza ed un messaggio che descrive l'esito
   *     dell'operazione
   */
  @PostMapping(value = "/visualizzaUtentiInAttesaInStanza/{id}")
  ResponseEntity<Response<List<Utente>>> visualizzaUtentiInAttesaInStanza(
      @PathVariable Long id, HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
      return stanzaService.visualizzaUtentiInAttesaInStanza(id, metaid);

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di visualizzazione di una stanza.
   *
   * @param id id della stanza da visualizzare
   * @param request richiesta HTTP fornita dal client
   * @return stanza da visualizzare ed un messaggio che descrive l'esito dell'operazione
   */
  @PostMapping(value = "/visualizzaStanza/{id}")
  public ResponseEntity<Response<Stanza>> visualizzaStanza(
      @PathVariable Long id, HttpServletRequest request) {

    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }
      Stanza s = stanzaService.visualizzaStanza(id);
      if (s != null) {
        return ResponseEntity.ok(new Response<>(s, "operazione effettuata con successo"));
      } else {
        throw new ClientRuntimeException("stanza non trovata, id non valido");
      }
    } catch (ClientRuntimeException ce) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new Response<>(null, "Errore durante la richiesta: " + ce.getMessage()));

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di visualizzazione di tutti gli scenari.
   *
   * @param request richiesta HTTP fornita dal client
   * @return lista di tutti gli scenari ed un messaggio che descrive l'esito dell'operazione
   */
  @GetMapping(value = "/visualizzaScenari")
  public ResponseEntity<Response<List<Scenario>>> visualizzaScenari(HttpServletRequest request) {
    List<Scenario> scenari;
    try {

      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      scenari = stanzaService.getAllScenari();
      if (scenari == null) {
        return ResponseEntity.status(500).body(new Response<>(null, "nessuno scenario creato"));
      } else {
        return ResponseEntity.ok(new Response<>(scenari, "operazione effettuata con successo"));
      }
    } catch (RuntimeException403 se) {
      return ResponseEntity.status(403)
          .body(new Response<>(null, "Errore durante l'operazione: " + se.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di visualizzazione dello scenario in uso in una
   * determinata stanza.
   *
   * @param id id della stanza di cui si vuole visualizzare lo scenario
   * @param request richiesta HTTP fornita dal client
   * @return scenario in uso nella stanza ed un messaggio che descrive l'esito dell'operazione
   */
  @PostMapping(value = "/visualizzaScenarioStanza/{id}")
  public ResponseEntity<Response<Scenario>> visualizzaScenarioStanza(
      @PathVariable Long id, HttpServletRequest request) {
    try {

      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      return stanzaService.findStanza(id);

    } catch (RuntimeException403 se) {
      return ResponseEntity.status(403)
          .body(new Response<>(null, "Errore durante l'operazione: " + se.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di modifica di uno scenario di una determinata
   * stanza.
   *
   * @param idStanza id della stanza di cui vogliamo modificare lo scenario
   * @param idScenario id del nuovo scenario da impostare nella stanza
   * @param request richiesta HTTP fornita dal client
   * @return valore booleano che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @PostMapping(value = "/modificaScenario/{idStanza}/{idScenario}")
  public ResponseEntity<Response<Boolean>> modificaScenario(
      @PathVariable Long idStanza, @PathVariable Long idScenario, HttpServletRequest request) {
    try {

      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
      return stanzaService.modificaScenario(metaid, idScenario, idStanza);

    } catch (RuntimeException403 e) {
      e.printStackTrace();
      return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di modifica del nome all'interno di una determinata
   * stanza di uno specifico utente.
   *
   * @param idStanza id della stanza in cui vogliamo modificare il nome dell'utente
   * @param idUtente id dell'utente a cui va modificato il nome all'interno della stanza
   * @param nome il nuovo nome da assegnare all'utente all'interno della stanza
   * @param request richiesta HTTP fornita dal client
   * @return valore booleano che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @PostMapping(value = "/modificaNomePartecipante/{idStanza}/{idUtente}")
  public ResponseEntity<Response<Boolean>> modificaNomePartecipante(
      @PathVariable Long idStanza,
      @PathVariable Long idUtente,
      @RequestBody String nome,
      HttpServletRequest request) {

    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(nome);
      String nuovoNome = jsonNode.get("nome").asText();

      return stanzaService.modificaNomePartecipante(metaid, idStanza, idUtente, nuovoNome);

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di kick di un partecipante da una determinata
   * stanza.
   *
   * @param idStanza id della stanza da cui vogliamo kickare un utente
   * @param idUtente id dell'utente da kickare
   * @param request richiesta HTTP fornita dal client
   * @return valore booleano che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @PostMapping(value = "/kickarePartecipante/{idStanza}/{idUtente}")
  public ResponseEntity<Response<Boolean>> kickPartecipante(
      @PathVariable Long idStanza, @PathVariable Long idUtente, HttpServletRequest request) {

    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      return stanzaService.kickPartecipante(metaid, idStanza, idUtente);

    } catch (RuntimeException403 e) {
      e.printStackTrace();
      return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di visualizzazione del ruolo dell' utente
   * all'interno di una specifica stanza.
   *
   * @param idStanza id della stanza
   * @param request richiesta HTTP fornita dal client
   * @return ruolo dell'utente all'interno della stanza ed un messaggio che descrive l'esito
   *     dell'operazione
   */
  @PostMapping(value = "/getRuolo/{idStanza}")
  public ResponseEntity<Response<Ruolo>> getRuoloByUserAndByStanza(
      @PathVariable Long idStanza, HttpServletRequest request) {
    try {

      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      Ruolo r = stanzaService.getRuoloByUserAndStanzaId(metaid, idStanza);
      if (r == null) {
        throw new ServerRuntimeException("errore nel recapito del ruolo");
      } else {
        return ResponseEntity.ok(new Response<>(r, "ruolo recapitato con successo"));
      }

    } catch (ServerRuntimeException | RuntimeException403 e) {

      // Gestisci le eccezioni e restituisci una risposta appropriata
      int statusCode = (e instanceof ServerRuntimeException) ? 500 : 403;
      return ResponseEntity.status(statusCode)
          .body(new Response<>(null, "Errore durante l'operazione: " + e.getMessage()));
    }
  }

  /**
   * Metodo per prelevare lo stato partecipazione dell'utente
   *
   * @param idStanza id della stanza
   * @param request richiesta http
   * @return ritorna una risposta con lo stato partecipazione dell'utente
   */
  @PostMapping(value = "/getStatopartecipazione/{idStanza}")
  public ResponseEntity<Response<StatoPartecipazione>> getStatoPartecipazione(
      @PathVariable Long idStanza, HttpServletRequest request) {
    try {

      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      StatoPartecipazione sp = stanzaService.getStatoPartecipazione(metaid, idStanza);
      if (sp == null) {
        throw new ServerRuntimeException("errore nel recapito del ruolo");
      } else {
        return ResponseEntity.ok(new Response<>(sp, "ruolo recapitato con successo"));
      }

    } catch (ServerRuntimeException | RuntimeException403 e) {

      // Gestisci le eccezioni e restituisci una risposta appropriata
      int statusCode = (e instanceof ServerRuntimeException) ? 500 : 403;
      return ResponseEntity.status(statusCode)
          .body(new Response<>(null, "Errore durante l'operazione: " + e.getMessage()));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di silenziare un determinato utente all'interno di
   * una specifica stanza.
   *
   * @param idStanza id della stanza in cui si vuole silenziare il partecipante
   * @param idUtente id del partecipante che si vuole silenziare
   * @param request richiesta HTTP fornita dal client
   * @return valore booleano che identifica la riuscita dell'operaizone ed un messaggio che descrive
   *     l'esito dei essa
   */
  @PostMapping(value = "/silenziarePartecipante/{idStanza}/{idUtente}")
  public ResponseEntity<Response<Boolean>> silenziaPartecipante(
      @PathVariable Long idStanza, @PathVariable Long idUtente, HttpServletRequest request) {

    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      return stanzaService.silenziaPartecipante(metaid, idStanza, idUtente);

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }

  /**
   * metodo che permette di gestire la richiesta di smutare un determinato utente all'interno di una
   * specifica stanza.
   *
   * @param idStanza id della stanza in cui si vuole smutare il partecipante
   * @param idUtente id del partecipante che si vuole smutare
   * @param request richiesta HTTP fornita dal client
   * @return valore booleano che identifica la riuscita dell'operaizone ed un messaggio che descrive
   *     l'esito dei essa
   */
  @PostMapping(value = "/unmutePartecipante/{idStanza}/{idUtente}")
  public ResponseEntity<Response<Boolean>> unmutePartecipante(
      @PathVariable Long idStanza, @PathVariable Long idUtente, HttpServletRequest request) {

    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaid = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      return stanzaService.unmutePartecipante(metaid, idStanza, idUtente);

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
    }
  }
}
