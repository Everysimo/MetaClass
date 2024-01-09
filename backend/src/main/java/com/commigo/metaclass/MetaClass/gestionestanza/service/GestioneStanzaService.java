package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException401;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.utility.response.types.AccessResponse;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface GestioneStanzaService
{

    ResponseEntity<AccessResponse<Boolean>> accessoStanza(String codiceStanza, String id_utente);
    boolean creaStanza(Stanza s) throws ServerRuntimeException, RuntimeException403;
    Response<Boolean> deleteRoom(String metaID, Long id_stanza);
    Response<Boolean> downgradeUtente(String id_Uogm, long og, long stanza);
    Boolean modificaDatiStanza(Map<String,Object> params, Long id) throws RuntimeException403, RuntimeException401;
    Stanza findStanza(Long id);
    List<StatoPartecipazione> findStatoPartecipazioniInAttesa(Stanza stanza,boolean isInAttesa);
    ResponseEntity<AccessResponse<Boolean>> richiestaAccessoStanza(String codiceStanza, String id_utente);
    StatoPartecipazione setStatoPartecipazione(Stanza stanza, Utente utente, boolean isInAttesa);
    Response<Boolean> upgradeUtente(String id_Uogm, long og, long stanza);
    ResponseEntity<Response<List<Utente>>> visualizzaUtentiInStanza(Long Id);
    ResponseEntity<Response<List<Utente>>> visualizzaUtentiInAttesaInStanza(Long Id, String metaID);

    Stanza visualizzaStanza(Long Id);
    List<Scenario> getAllScenari();
    Scenario visualizzaScenarioStanza(Stanza stanza);

    ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(Long Id);

    ResponseEntity<Response<Boolean>> modificaScenario(String metaID, Long idScenario, Long idStanza);
}