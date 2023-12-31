package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class GestioneStanzaControl {

   // @Autowired
    //@Qualifier("GestioneStanzaService")
    //private GestioneStanzaService stanzaService;

    /*
    @PostMapping(value = "/richiestaAccessoStanza")
    public ResponseEntity<RispostaRichiestaAccessoStanza> richiestaAccessoStanza(Utente u, String codiceStanza, HttpSession session) {
        try {
            if (!stanzaService.richiestaAccessoStanza(codiceStanza, u.getId())) {
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

    */

}
