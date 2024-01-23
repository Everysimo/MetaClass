package com.commigo.metaclass.gestionestimaduratameeting.controller;

import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.gestionestimaduratameeting.service.GestioneStimaMeetingService;
import com.commigo.metaclass.utility.response.types.Response;
import com.commigo.metaclass.webconfig.ValidationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/** Metodo che gestisce le richieste relative al modulo AI. */
@RestController
public class StimaDurataMeetingController {

  @Autowired private ValidationToken validationToken;

  @Autowired private GestioneStimaMeetingService gestioneStimaMeetingService;

  /**
   * Metodo che permette di gestire la richiesta di visualizzazione della stima della durata di unn
   * meeting.
   *
   * @param idStanza id della stanza in cui deve essere schedulato il meeting
   * @param request richiesta HTTP fornita dal client
   * @return ritorna la durata stimata del meeting
   */
  @GetMapping(value = "/stimaMeeting/{idStanza}")
  public ResponseEntity<Response<Double>> visualizzaStimaDurataMeeting(
      @PathVariable Long idStanza, HttpServletRequest request) {

    try {

      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      double durata = gestioneStimaMeetingService.getDurataMeeting(idStanza);
      return ResponseEntity.ok(new Response<>(durata, "Stima effettuata con successo"));

    } catch (RuntimeException403 re) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
    } catch (ServerRuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new Response<>(null, "Errore durante la richiesta: " + e.getMessage()));
    }
  }
}
