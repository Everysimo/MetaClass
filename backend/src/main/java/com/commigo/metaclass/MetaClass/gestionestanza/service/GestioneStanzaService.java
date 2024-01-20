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

public interface GestioneStanzaService {

  ResponseEntity<AccessResponse<Long>> accessoStanza(String codiceStanza, String id_utente)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<Boolean>> banPartecipante(Long IdStanza, String metaId, Long IdUtente)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<Boolean>> banUtente(Long IdStanza, String metaId, Long IdUtente)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<Boolean>> banOrganizzatore(Long IdStanza, String metaId, Long IdUtente)
      throws ServerRuntimeException, RuntimeException403;

  boolean creaStanza(Stanza s, String metaID) throws Exception;

  Response<Boolean> deleteRoom(String metaID, Long id_stanza);

  Response<Boolean> downgradeUtente(String id_Uogm, long og, long stanza)
      throws ServerRuntimeException, RuntimeException403;

  Boolean modificaDatiStanza(Map<String, Object> params, Long id)
      throws RuntimeException403, RuntimeException401;

  ResponseEntity<Response<Scenario>> findStanza(Long id);

  Response<Boolean> upgradeUtente(String id_Uogm, long og, long stanza)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<List<Utente>>> visualizzaUtentiInStanza(Long Id);

  ResponseEntity<Response<List<Utente>>> visualizzaUtentiInAttesaInStanza(Long Id, String metaID);

  Stanza visualizzaStanza(Long Id);

  List<Scenario> getAllScenari();

  ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(Long Id);

  ResponseEntity<Response<Boolean>> modificaScenario(String metaID, Long idScenario, Long idStanza);

  ResponseEntity<Response<Boolean>> modificaNomePartecipante(
      String metaID, Long idStanza, Long idUtente, String nome);

  ResponseEntity<Response<Boolean>> kickPartecipante(String metaID, Long idStanza, Long idUtente);

  Ruolo getRuoloByUserAndStanzaID(String metaID, Long idStanza)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<Boolean>> gestioneAccesso(
      String metaID, Long idUtente, Long idStanza, boolean scelta);

  ResponseEntity<Response<Boolean>> SilenziaPartecipante(
      String metaID, Long IdStanza, Long IdUtente);

  ResponseEntity<Response<Boolean>> UnmutePartecipante(String metaID, Long IdStanza, Long IdUtente);
  // ResponseEntity<Response<Boolean>> Unmute(String metaID, Long IdStanza);
  // ResponseEntity<Response<Boolean>> mute(String metaID, Long IdStanza);
}
