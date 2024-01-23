package com.commigo.metaclass.gestionestanza.service;

import com.commigo.metaclass.entity.*;
import com.commigo.metaclass.exceptions.RuntimeException401;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.utility.response.types.AccessResponse;
import com.commigo.metaclass.utility.response.types.Response;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/** Interfaccia che offre servizi legati alle stanze. */
public interface GestioneStanzaService {

  ResponseEntity<AccessResponse<Long>> accessoStanza(String codiceStanza, String idUtente)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<Boolean>> banPartecipante(Long idStanza, String metaId, Long idUtente)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<Boolean>> banUtente(Long idStanza, String metaId, Long idUtente)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<Boolean>> banOrganizzatore(Long idStanza, String metaId, Long idUtente)
      throws ServerRuntimeException, RuntimeException403;

  boolean creaStanza(Stanza s, String metaId) throws Exception;

  Response<Boolean> deleteRoom(String metaId, Long idStanza);

  Response<Boolean> downgradeUtente(String idUogm, long og, long stanza)
      throws ServerRuntimeException, RuntimeException403;

  Boolean modificaDatiStanza(Map<String, Object> params, Long id)
      throws RuntimeException403, RuntimeException401;

  ResponseEntity<Response<Scenario>> findStanza(Long id);

  Response<Boolean> upgradeUtente(String idUogm, long og, long stanza)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<List<Utente>>> visualizzaUtentiInStanza(Long id);

  ResponseEntity<Response<List<Utente>>> visualizzaUtentiInAttesaInStanza(Long id, String metaId);

  Stanza visualizzaStanza(Long id);

  List<Scenario> getAllScenari();

  ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(Long id);

  ResponseEntity<Response<Boolean>> modificaScenario(String metaId, Long idScenario, Long idStanza);

  ResponseEntity<Response<Boolean>> modificaNomePartecipante(
      String metaId, Long idStanza, Long idUtente, String nome);

  ResponseEntity<Response<Boolean>> kickPartecipante(String metaId, Long idStanza, Long idUtente);

  Ruolo getRuoloByUserAndStanzaId(String metaId, Long idStanza)
      throws ServerRuntimeException, RuntimeException403;

  List<StatoPartecipazione> getStatoPartecipazione(String metaId, Long idStanza)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<Boolean>> gestioneAccesso(
      String metaId, Long idUtente, Long idStanza, boolean scelta);

  ResponseEntity<Response<Boolean>> silenziaPartecipante(
      String metaId, Long idStanza, Long idUtente);

  ResponseEntity<Response<Boolean>> unmutePartecipante(String metaId, Long idStanza, Long idUtente);
  // ResponseEntity<Response<Boolean>> Unmute(String metaId, Long idStanza);
  // ResponseEntity<Response<Boolean>> mute(String metaId, Long idStanza);
}
