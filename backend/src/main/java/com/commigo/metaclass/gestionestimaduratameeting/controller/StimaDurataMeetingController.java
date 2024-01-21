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

@RestController
public class StimaDurataMeetingController {

  @Autowired private ValidationToken validationToken;

  @Autowired private GestioneStimaMeetingService gestioneStimaMeetingService;

  /**
   * meotdo che permette di gestire la richiesta di visualizzazione della stima della durata di unn
   * meeting
   *
   * @param id_stanza id della stanza in cui deve essere schedulato il meeting
   * @param request richiesta HTTP fornita dal client
   * @return
   */
  @GetMapping(value = "/stimaMeeting/{id_stanza}")
  public ResponseEntity<Response<Double>> visualizzaStimaDurataMeeting(
      @PathVariable Long id_stanza, HttpServletRequest request) {

    try {

      // validazione dl token
      if (!validationToken.isTokenValid(request)) {
        throw new RuntimeException403("Token non valido");
      }

      double durata = gestioneStimaMeetingService.getDurataMeeting(id_stanza);
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
