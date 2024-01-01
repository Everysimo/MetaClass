package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.*;

@RestController
public class GestioneStanzaControl {

    @Autowired
    @Qualifier("GestioneStanzaService")
    private GestioneStanzaService stanzaService;

    @PostMapping(value = "/accessoStanza")
    public ResponseEntity<RispostaRichiestaAccessoStanza> richiestaAccessoStanza(@RequestBody String requestBody, HttpSession session) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            String codiceStanza = jsonNode.get("codice").asText();

            return ResponseEntity.ok(stanzaService.accessoStanza(codiceStanza, (String) session.getAttribute("UserMetaID")).getBody());

        } catch (RuntimeException | JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RispostaRichiestaAccessoStanza(false, "Errore durante la richiesta: " + e.getMessage()));
        }
    }


}
