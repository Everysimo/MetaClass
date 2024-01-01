package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;
import org.springframework.http.ResponseEntity;

public interface GestioneStanzaService {
    public ResponseEntity<ResponseBoolMessage> richiestaAccessoStanza(String codiceStanza, String id_utente);
    public ResponseEntity<ResponseBoolMessage> accessoStanza (String codiceStanza, String id_utente);
}
