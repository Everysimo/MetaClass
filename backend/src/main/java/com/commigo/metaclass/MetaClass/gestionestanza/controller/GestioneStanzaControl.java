package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GestioneStanzaControl {

    @Autowired
    @Qualifier("GestioneStanzaService")
    private GestioneStanzaService stanzaService;

    @PostMapping(value = "/richiestaAccessoStanza")
    public ResponseEntity<RispostaRichiestaAccessoStanza> richiestaAccessoStanza(@RequestBody String codiceStanza, HttpSession session) {
        try {

            if (!stanzaService.richiestaAccessoStanza(codiceStanza, (String) session.getAttribute("UserMetaID"))) {
                throw new RuntimeException("Richiesta non effettuato");
            } else {
                // Utente in attesa
                return ResponseEntity.ok(new RispostaRichiestaAccessoStanza(true, "Utente in attesa"));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RispostaRichiestaAccessoStanza(false, "Errore durante la richiesta: " + e.getMessage()));
        }
    }


}
