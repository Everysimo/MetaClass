package com.commigo.metaclass.gestionemeeting.controller;

import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.exceptions.RuntimeException401;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.utility.MapValidator;
import com.commigo.metaclass.utility.request.RequestUtils;
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

/** Gestione meeting. */
@RestController
public class GestioneMeetingController {

  @Autowired private GestioneMeetingService meetingService;

  @Autowired private ValidationToken validationToken;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  /**
   * Metodo che permette di gestire la richiesta di schedulazione di un meeting.
   *
   * @param m Meeting che deve essere schedulato
   * @param result Variabile che contiene tutti gli errori di validazione dell'oggetto meeting
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/schedulingMeeting")
  public ResponseEntity<Response<Boolean>> schedulingMeeting(
      @Valid @RequestBody Meeting m, BindingResult result, HttpServletRequest request) {
    try {

      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      // controlla se i parametri passati al meeting sono corretti
      if (result.hasErrors()) {
        throw new RuntimeException403(RequestUtils.errorsRequest(result));
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      meetingService.creaScheduling(m, metaId);
      return ResponseEntity.ok(new Response<>(true, "Meeting schedulato con successo"));

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403)
          .body(
              new Response<>(
                  false, "Errore durante la schedulazione del meeting: " + e.getMessage()));
    } catch (ServerRuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new Response<>(
                  false, "Errore durante la schedulazione del meeting: " + e.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di modifica di schedulazione di un meeting.
   *
   * @param id id Meeting sul quale modificare la schedulazione
   * @param params variabile che contiene i parametri da modificare del meeting
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/modifyScheduling/{id}")
  public ResponseEntity<Response<Boolean>> modifyScheduling(
      @PathVariable Long id, @RequestBody Map<String, Object> params, HttpServletRequest request) {

    try {
      // controllo del token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      // validazione della map
      MapValidator.meetingValidate(params);

      if (!meetingService.modificaScheduling(params, id)) {
        throw new ServerRuntimeException("modifica non effettuata");
      } else {
        return ResponseEntity.ok(new Response<>(true, "Meeting modificata con successo"));
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
   * Metodo che permette di gestire la richiesta di avvio di un meeting.
   *
   * @param idMeeting ID del meeting che deve essere avviato
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/avviaMeeting/{idMeeting}")
  public ResponseEntity<Response<Boolean>> avviaMeeting(
      @PathVariable Long idMeeting, HttpServletRequest request) {
    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      if (meetingService.avviaMeeting(metaId, idMeeting)) {
        return ResponseEntity.ok(new Response<>(true, "Avvio meeting avvenuto con successo"));
      } else {
        throw new ServerRuntimeException("Errore nell'avvio del meeting");
      }

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(false, se.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di visualizzazione dei meeting schedulati
   * all'iterno di una stanza.
   *
   * @param id Id della stanza di cui bisogna visualizzare i meeting schedulati
   * @param request richiesta HTTP fornita dal client
   * @return una lista di meeting ed un messaggio che descrive l'esito dell'operazione
   */
  @PostMapping(value = "/visualizzaSchedulingMeeting/{id}")
  public ResponseEntity<Response<List<Meeting>>> visualizzaSchedulingMeeting(
      @PathVariable Long id, HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      return meetingService.visualizzaSchedulingMeeting(id);

    } catch (Exception e) {
      return ResponseEntity.ok(
          new Response<>(null, "Errore visualizzazione Scheduling dei meeting per la stanza"));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di accesso ad un meeting.
   *
   * @param idMeeting id del meeting a cui si vuole fare accesso
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/accediMeeting/{idMeeting}")
  public ResponseEntity<Response<Boolean>> accediMeeting(
      @PathVariable Long idMeeting, HttpServletRequest request) {
    try {

      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      if (meetingService.accediMeeting(metaId, idMeeting)) {
        return ResponseEntity.ok(new Response<>(true, "Accesso avvenuto con successo"));
      } else {
        throw new ServerRuntimeException("Errore nell'accesso al meeting");
      }
    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(false, se.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di terminazione di un meeting.
   *
   * @param idMeeting id del meeting che si vuole teminare
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/terminaMeeting/{idMeeting}")
  public ResponseEntity<Response<Boolean>> terminaMeeting(
      @PathVariable Long idMeeting, HttpServletRequest request) {
    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      if (meetingService.terminaMeeting(metaId, idMeeting)) {
        return ResponseEntity.ok(new Response<>(true, "Meeting terminato con successo"));
      } else {
        throw new ServerRuntimeException("Errore nella terminazione del meeting");
      }

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(false, se.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richista di uscita da un determianto meeting.
   *
   * @param idMeeting id del meeting da cui si vuole uscire
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/uscitaMeeting/{idMeeting}")
  public ResponseEntity<Response<Boolean>> uscitaMeeting(
      @PathVariable Long idMeeting, HttpServletRequest request) {
    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      if (meetingService.uscitaMeeting(metaId, idMeeting)) {
        return ResponseEntity.ok(new Response<>(true, "Uscita avvenuta con successo"));
      } else {
        throw new ServerRuntimeException("Errore nell'uscita dell'utente del meeting");
      }

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(false, se.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di visualizzazione del questionario.
   *
   * @param request richiesta HTTP fornita dal client
   * @returnun una lista di meeting in cui l'utente deve compilare in questionario ed un messaggio
   *     che descrive l'esito di essa
   */
  @GetMapping("/visualizzaQuestionari")
  public ResponseEntity<Response<List<Meeting>>> visualizzaQuestionario(
      HttpServletRequest request) {

    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
      List<Meeting> meetingToQuest = meetingService.visualizzaQuestionari(metaId);
      return ResponseEntity.ok(
          new Response<>(
              meetingToQuest,
              "La stampa dei meeting su cui bisogna compilare il questionario è"
                  + " avvenuto con successo"));

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(null, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(null, se.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di compilazione del questionario.
   *
   * @param jsonValue Valori inseriti dall'utente all'interno del questionario
   * @param idMeeting id del metting a cui fa riferimento il questionario
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping("/compilaQuestionario/{idMeeting}")
  public ResponseEntity<Response<Boolean>> compilaQuestionario(
      @RequestBody String jsonValue, @PathVariable Long idMeeting, HttpServletRequest request) {

    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(jsonValue);
      JsonNode valutazioneNode = jsonNode.get("immersionLevel");

      // validazione del livello di immersività
      int value;
      if (valutazioneNode.isNull()) {
        throw new RuntimeException403("inserire 'immersionLevel' come nome dell'attributo");
      }
      if (!valutazioneNode.isInt()) {
        throw new RuntimeException403("inserire un valore di immersivita' intero [1,5]");
      }
      value = valutazioneNode.asInt();
      if (value < 1 || value > 5) {
        throw new RuntimeException403("valore di immersivita' non valido");
      }

      JsonNode motionSicknessNode = jsonNode.get("motionSickness");

      // validazione motionsickness
      int motionSickness;
      if (motionSicknessNode.isNull()) {
        throw new RuntimeException403("inserire 'motionSickness' come nome dell'attributo");
      }
      if (!motionSicknessNode.isInt()) {
        throw new RuntimeException403("inserire un valore di motionSickness intero [1,10]");
      }
      motionSickness = motionSicknessNode.asInt();
      if (motionSickness < 1 || motionSickness > 10) {
        throw new RuntimeException403("valore di motionSickness non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      meetingService.compilaQuestionario(value, motionSickness, metaId, idMeeting);

      return ResponseEntity.ok(new Response<>(true, "questionario compilato con successo"));

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(null, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(null, se.getMessage()));
    } catch (JsonProcessingException e) {
      return ResponseEntity.status(403)
          .body(
              new Response<>(
                  null,
                  "'valutazione' o altri attributi non sono "
                      + "disponibili: (immersionLevel,motionSickness) "));
    }
  }

  /**
   * Metodo che permette di gestire la visualizzazione dei meeting precedenti a cui ha partecipato
   * l'utente.
   *
   * @param request richiesta dal client.
   * @return una lista di meeting in cui e un messaggio che descrive l'esito dell'operazione
   */
  @GetMapping("/visualizzaMeetingPrecedenti")
  public ResponseEntity<Response<List<Meeting>>> visualizzaMeetingPrecedeni(
      HttpServletRequest request) {

    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

      List<Meeting> meetingToQuest = meetingService.getMeetingPrecedenti(metaId);
      return ResponseEntity.ok(new Response<>(meetingToQuest, "Operazione avvenuta con successo"));

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(null, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(null, se.getMessage()));
    }
  }
}
