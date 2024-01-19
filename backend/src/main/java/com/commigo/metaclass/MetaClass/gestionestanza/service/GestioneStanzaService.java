package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.*;
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

    /**
     *
     * @param codiceStanza
     * @param id_utente
     * @return
     * @throws Exception
     */
    ResponseEntity<AccessResponse<Long>> accessoStanza(String codiceStanza, String id_utente) throws Exception;

    /**
     *
     * @param IdStanza
     * @param metaId
     * @param IdUtente
     * @return
     * @throws ServerRuntimeException
     * @throws RuntimeException403
     */
    ResponseEntity<Response<Boolean>> banPartecipante(Long IdStanza, String metaId, Long IdUtente)  throws ServerRuntimeException, RuntimeException403;

    /**
     *
     * @param IdStanza
     * @param metaId
     * @param IdUtente
     * @return
     * @throws ServerRuntimeException
     * @throws RuntimeException403
     */
    ResponseEntity<Response<Boolean>> banUtente(Long IdStanza, String metaId, Long IdUtente)  throws ServerRuntimeException, RuntimeException403;

    /**
     *
     * @param IdStanza
     * @param metaId
     * @param IdUtente
     * @return
     * @throws ServerRuntimeException
     * @throws RuntimeException403
     */
    ResponseEntity<Response<Boolean>> banOrganizzatore(Long IdStanza, String metaId, Long IdUtente)  throws ServerRuntimeException, RuntimeException403;

    /**
     *
     * @param s
     * @param metaID
     * @return
     * @throws Exception
     */
    boolean creaStanza(Stanza s, String metaID) throws Exception;

    /**
     *
     * @param metaID
     * @param id_stanza
     * @return
     */
    Response<Boolean> deleteRoom(String metaID, Long id_stanza);

    /**
     *
     * @param id_Uogm
     * @param og
     * @param stanza
     * @return
     * @throws ServerRuntimeException
     * @throws RuntimeException403
     */
    Response<Boolean> downgradeUtente(String id_Uogm, long og, long stanza) throws ServerRuntimeException, RuntimeException403;

    /**
     *
     * @param params
     * @param id
     * @return
     * @throws RuntimeException403
     * @throws RuntimeException401
     */
    Boolean modificaDatiStanza(Map<String,Object> params, Long id) throws RuntimeException403, RuntimeException401;

    /**
     *
     * @param id
     * @return
     */
    ResponseEntity<Response<Scenario>> findStanza(Long id);

    /**
     *
     * @param id_Uogm
     * @param og
     * @param stanza
     * @return
     * @throws ServerRuntimeException
     * @throws RuntimeException403
     */
    Response<Boolean> upgradeUtente(String id_Uogm, long og, long stanza) throws ServerRuntimeException, RuntimeException403;

    /**
     *
     * @param Id
     * @return
     */
    ResponseEntity<Response<List<Utente>>> visualizzaUtentiInStanza(Long Id);

    /**
     *
     * @param Id
     * @param metaID
     * @return
     */
    ResponseEntity<Response<List<Utente>>> visualizzaUtentiInAttesaInStanza(Long Id, String metaID);

    /**
     *
     * @param Id
     * @return
     */
    Stanza visualizzaStanza(Long Id);

    /**
     *
     * @return
     */
    List<Scenario> getAllScenari();

    /**
     *
     * @param Id
     * @return
     */
    ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(Long Id);

    /**
     *
     * @param metaID
     * @param idScenario
     * @param idStanza
     * @return
     */
    ResponseEntity<Response<Boolean>> modificaScenario(String metaID, Long idScenario, Long idStanza);

    /**
     *
     * @param metaID
     * @param idStanza
     * @param idUtente
     * @param nome
     * @return
     */
    ResponseEntity<Response<Boolean>> modificaNomePartecipante(String metaID, Long idStanza, Long idUtente, String nome);

    /**
     *
     * @param metaID
     * @param idStanza
     * @param idUtente
     * @return
     */
    ResponseEntity<Response<Boolean>> kickPartecipante(String metaID, Long idStanza, Long idUtente);

    /**
     *
     * @param metaID
     * @param idStanza
     * @return
     * @throws ServerRuntimeException
     * @throws RuntimeException403
     */
    Ruolo getRuoloByUserAndStanzaID(String metaID, Long idStanza) throws ServerRuntimeException, RuntimeException403;

    /**
     *
     * @param metaID
     * @param idUtente
     * @param idStanza
     * @param scelta
     * @return
     */
    ResponseEntity<Response<Boolean>> gestioneAccesso(String metaID, Long idUtente, Long idStanza, boolean scelta);

    /**
     *
      * @param metaID
     * @param IdStanza
     * @param IdUtente
     * @return
     */
    ResponseEntity<Response<Boolean>> SilenziaPartecipante(String metaID, Long IdStanza, Long IdUtente);

    ResponseEntity<Response<Boolean>> UnmutePartecipante(String metaID, Long IdStanza, Long IdUtente);

    /*ResponseEntity<Response<Boolean>> Unmute(String metaID, Long IdStanza);
    ResponseEntity<Response<Boolean>> mute(String metaID, Long IdStanza);
    */
}