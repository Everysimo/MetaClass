package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.utility.response.types.AccessResponse;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface GestioneStanzaService
{

    ResponseEntity<AccessResponse<Boolean>> accessoStanza(String codiceStanza, String id_utente);
    boolean creaStanza(Stanza s);
    Response<Boolean> deleteRoom(String id_Uogm, Long id_stanza);
    Response<Boolean> downgradeUtente(String id_Uogm, long og, long stanza);
    Response<Boolean> modificaDatiStanza(String id, Long Id, Map<String, Object> dataMap, Stanza stanza);
    Stanza findStanza(Long id);
    List<StatoPartecipazione> findStatoPartecipazioniInAttesa(Stanza stanza,boolean isInAttesa);
    ResponseEntity<AccessResponse<Boolean>> richiestaAccessoStanza(String codiceStanza, String id_utente);
    StatoPartecipazione setStatoPartecipazione(Stanza stanza, Utente utente, boolean isInAttesa);
    Response<Boolean> upgradeUtente(String id_Uogm, long og, long stanza);
    List<Utente> visualizzaStanza(Long Id);
}