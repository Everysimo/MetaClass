package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.gestionestanza.controller.RispostaRichiestaAccessoStanza;
import org.springframework.http.ResponseEntity;

public interface GestioneStanzaService {
    public ResponseEntity<RispostaRichiestaAccessoStanza> richiestaAccessoStanza(String codiceStanza, String id_utente);
    public ResponseEntity<RispostaRichiestaAccessoStanza> accessoStanza (String codiceStanza, String id_utente);
}
