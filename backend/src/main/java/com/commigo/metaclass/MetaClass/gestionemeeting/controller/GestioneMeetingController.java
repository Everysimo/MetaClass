package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GestioneMeetingController {

  @Autowired private GestioneMeetingService meetingService;

  @Autowired private ValidationToken validationToken;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  /**
   * Metodo che permette di gestire la richiesta di schedulazione di un meeting
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

      String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

      meetingService.creaScheduling(m, metaID);
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
   * Metodo che permette di gestire la richiesta di modifica di schedulazione di un meeting
   *
   * @param m Meeting sul quale modificare la schedulazione
   * @param result variabile che contiene tutti gli errori di validazione dell'oggetto meeting
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/modificaScheduling")
  public ResponseEntity<Response<Boolean>> modificaScheduling(
      @Valid @RequestBody Meeting m, BindingResult result, HttpServletRequest request) {

    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      // controllo errori di validazione
      if (result.hasErrors()) {
        return ResponseEntity.status(403)
            .body(new Response<>(false, RequestUtils.errorsRequest(result)));
      }

      if (!meetingService.modificaScheduling(m)) {
        throw new ServerRuntimeException("modifica non effettuata");
      } else {
        return ResponseEntity.ok(new Response<>(true, "Meeting schedulato con successo"));
      }

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(false, se.getMessage()));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di avvio di un meeting
   *
   * @param id_meeting ID del meeting che deve essere avviato
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/avviaMeeting/{id_meeting}")
  public ResponseEntity<Response<Boolean>> avviaMeeting(
      @PathVariable Long id_meeting, HttpServletRequest request) {
    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

      if (meetingService.avviaMeeting(metaID, id_meeting)) {
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
   * all'iterno di una stanza
   *
   * @param Id Id della stanza di cui bisogna visualizzare i meeting schedulati
   * @param request richiesta HTTP fornita dal client
   * @return una lista di meeting ed un messaggio che descrive l'esito dell'operazione
   */
  @PostMapping(value = "/visualizzaSchedulingMeeting/{Id}")
  public ResponseEntity<Response<List<Meeting>>> visualizzaSchedulingMeeting(
      @PathVariable Long Id, HttpServletRequest request) {
    try {
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      return meetingService.visualizzaSchedulingMeeting(Id);

    } catch (Exception e) {
      return ResponseEntity.ok(
          new Response<>(null, "Errore visualizzazione Scheduling dei meeting per la stanza"));
    }
  }

  /**
   * Metodo che permette di gestire la richiesta di accesso ad un meeting
   *
   * @param id_meeting id del meeting a cui si vuole fare accesso
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/accediMeeting/{id_meeting}")
  public ResponseEntity<Response<Boolean>> accediMeeting(
      @PathVariable Long id_meeting, HttpServletRequest request) {
    try {

      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

      if (meetingService.accediMeeting(metaID, id_meeting)) {
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
   * Metodo che permette di gestire la richiesta di terminazione di un meeting
   *
   * @param id_meeting id del meeting che si vuole teminare
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/terminaMeeting/{id_meeting}")
  public ResponseEntity<Response<Boolean>> terminaMeeting(
      @PathVariable Long id_meeting, HttpServletRequest request) {
    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

      if (meetingService.terminaMeeting(metaID, id_meeting)) {
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
   * Metodo che permette di gestire la richista di uscita da un determianto meeting
   *
   * @param id_meeting id del meeting da cui si vuole uscire
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping(value = "/uscitaMeeting/{id_meeting}")
  public ResponseEntity<Response<Boolean>> uscitaMeeting(
      @PathVariable Long id_meeting, HttpServletRequest request) {
    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

      if (meetingService.uscitaMeeting(metaID, id_meeting)) {
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
   * Metodo che permette di gestire la richiesta di visualizzazione del questionario
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

      String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());
      List<Meeting> meetingToQuest = meetingService.visualizzaQuestionari(metaID);
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
   * Metodo che permette di gestire la richiesta di compilazione del questionario
   *
   * @param JSONvalue Valori inseriti dall'utente all'interno del questionario
   * @param id_meeting id del metting a cui fa riferimento il questionario
   * @param request richiesta HTTP fornita dal client
   * @return un valore booleano che identifica la riuscita dell'operazione ed un messaggio che
   *     descrive l'esito di essa
   */
  @PostMapping("/compilaQuestionario/{id_meeting}")
  public ResponseEntity<Response<Boolean>> compilaQuestionario(
      @RequestBody String JSONvalue, @PathVariable Long id_meeting, HttpServletRequest request) {

    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(JSONvalue);
      JsonNode valutazioneNode = jsonNode.get("immersionLevel");
      JsonNode motionSicknessNode = jsonNode.get("motionSickness");

      // validazione del livello di immersività
      int value;
      if (valutazioneNode.isNull())
        throw new RuntimeException403("inserire 'immersionLevel' come nome dell'attributo");
      if (!valutazioneNode.isInt())
        throw new RuntimeException403("inserire un valore di immersivita' intero [1,5]");
      value = valutazioneNode.asInt();
      if (value < 1 || value > 5) {
        throw new RuntimeException403("valore di immersivita' non valido");
      }

      // validazione motionsickness
      int motionSickness;
      if (motionSicknessNode.isNull())
        throw new RuntimeException403("inserire 'motionSickness' come nome dell'attributo");
      if (!motionSicknessNode.isInt())
        throw new RuntimeException403("inserire un valore di motionSickness intero [1,10]");
      motionSickness = motionSicknessNode.asInt();
      if (motionSickness < 1 || motionSickness > 10) {
        throw new RuntimeException403("valore di motionSickness non valido");
      }

      String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

      meetingService.compilaQuestionario(value, motionSickness, metaID, id_meeting);

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
   * Metodo che permette di gestire la visualizzazione dei metting precedenti a cui ha partecipato
   * l'utente
   *
   * @param request
   * @return una lista di meeting in cui ed un messaggio che descrive l'esito dell'operazione
   */
  @GetMapping("/visualizzaMeetingPrecedenti")
  public ResponseEntity<Response<List<Meeting>>> visualizzaMeetingPrecedeni(
      HttpServletRequest request) {

    try {
      // controllo token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

      List<Meeting> meetingToQuest = meetingService.getMeetingPrecedenti(metaID);
      return ResponseEntity.ok(new Response<>(meetingToQuest, "Operazione avvenuta con successo"));

    } catch (RuntimeException403 e) {
      return ResponseEntity.status(403).body(new Response<>(null, e.getMessage()));
    } catch (ServerRuntimeException se) {
      return ResponseEntity.status(500).body(new Response<>(null, se.getMessage()));
    }
  }
}
