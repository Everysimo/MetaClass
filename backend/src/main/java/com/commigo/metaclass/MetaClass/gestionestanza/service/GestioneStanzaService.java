package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.utility.response.types.AccessResponse;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GestioneStanzaService {
    ResponseEntity<Response<Boolean>> accessoStanza(String codiceStanza, String id_utente);

    /**
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

    ResponseEntity<Response<Boolean>> richiestaAccessoStanza(String codiceStanza, String id_utente);

    Response<Boolean> upgradeUtente(String id_Uogm, long og, long stanza);

    Response<Boolean> downgradeUtente(String id_Uogm, long og, long stanza);

    Response<Boolean> deleteRoom(String id_Uogm, Long id_stanza);

    Response<Boolean> modificaDatiStanza(String id, Long Id, Map<String, Object> dataMap, Stanza stanza);
}