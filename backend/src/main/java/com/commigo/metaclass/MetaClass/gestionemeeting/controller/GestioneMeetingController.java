package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GestioneMeetingController {

    @Autowired
    @Qualifier("GestioneMeetingService")
    private GestioneMeetingService meetingService;
    @PostMapping(value = "/schedulingMeeting")
    public ResponseEntity<ResponseBoolMessage> schedulingMeeting(@RequestBody Meeting m) {
        try {
            if (!meetingService.creaScheduling(m)) {
                throw new RuntimeException("Meeting non effettuato");
            } else {
                return ResponseEntity.ok(new ResponseBoolMessage(true, "Meeting schedulato con successo"));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseBoolMessage(false, "Errore durante il login: " + e.getMessage()));
        }
    }
}
