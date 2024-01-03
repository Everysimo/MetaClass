package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;
import org.springframework.http.ResponseEntity;

public interface GestioneStanzaService
{
    ResponseEntity<ResponseBoolMessage> accessoStanza (String codiceStanza, String id_utente);

    /**
     *
     * Questo metodo crea una nuova stanza.
     * La istanza della stanza creata viene ritornata.
     *
     * @param nome
     * @param Codice_Stanza
     * @param Descrizione
     * @param Tipo_Accesso
     * @param MAX_Posti
     * @return
     */
    Stanza creaStanza(String nome, String Codice_Stanza, String Descrizione, boolean Tipo_Accesso, int MAX_Posti);

    ResponseEntity<ResponseBoolMessage> richiestaAccessoStanza(String codiceStanza, String id_utente);
    ResponseBoolMessage upgradeUtente(String id_Uogm, long og, long stanza);

}
