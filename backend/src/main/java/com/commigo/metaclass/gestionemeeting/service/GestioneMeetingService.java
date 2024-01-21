package com.commigo.metaclass.gestionemeeting.service;

import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.utility.response.types.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GestioneMeetingService {

  public boolean creaScheduling(Meeting meeting, String metaId)
      throws ServerRuntimeException, RuntimeException403;

  public boolean modificaScheduling(Meeting meeting)
      throws ServerRuntimeException, RuntimeException403;

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
