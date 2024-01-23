package com.commigo.metaclass.gestionemeeting.service;

import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.exceptions.RuntimeException401;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.utility.response.types.Response;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/** Interfaccia che offre servizi legati ai meetings. */
public interface GestioneMeetingService {

  public boolean creaScheduling(Meeting meeting, String metaId)
      throws ServerRuntimeException, RuntimeException403;

  boolean modificaScheduling(Map<String, Object> params, Long id)
      throws RuntimeException403, RuntimeException401;

  Boolean accediMeeting(String metaId, Long idmeeting)
      throws ServerRuntimeException, RuntimeException403;

  Boolean avviaMeeting(String metaId, Long idmeeting)
      throws ServerRuntimeException, RuntimeException403;

  ResponseEntity<Response<List<Meeting>>> visualizzaSchedulingMeeting(Long idStanza);

  Boolean terminaMeeting(String metaId, Long idmeeting)
      throws ServerRuntimeException, RuntimeException403;

  Boolean uscitaMeeting(String metaId, Long idmeeting)
      throws ServerRuntimeException, RuntimeException403;

  List<Meeting> visualizzaQuestionari(String metaId)
      throws ServerRuntimeException, RuntimeException403;

  List<Meeting> getMeetingPrecedenti(String metaId)
      throws ServerRuntimeException, RuntimeException403;

  boolean compilaQuestionario(Integer value, Integer motionSickness, String metaId, Long idmeeting)
      throws ServerRuntimeException, RuntimeException403;
}
