package com.commigo.metaclass.MetaClass.gestionemeeting.service;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface GestioneMeetingService {
    public boolean creaScheduling(Meeting meeting, String metaID) throws ServerRuntimeException, RuntimeException403;
    public boolean modificaScheduling(Meeting meeting) throws ServerRuntimeException, RuntimeException403;
    Meeting findMeetingById(Long id);
    Meeting saveMeeting(Meeting meeting);
    Boolean accediMeeting(String metaID, Long id_meeting) throws ServerRuntimeException, RuntimeException403;
    Boolean avviaMeeting(String metaID, Long id_meeting) throws ServerRuntimeException, RuntimeException403;
    ResponseEntity<Response<List<Meeting>>> visualizzaSchedulingMeeting(Long idStanza);
    Boolean terminaMeeting(String metaID, Long id_meeting) throws ServerRuntimeException, RuntimeException403;
    Boolean uscitaMeeting(String metaID, Long id_meeting) throws ServerRuntimeException, RuntimeException403;

    List<Meeting> visualizzaQuestionari(String metaId) throws ServerRuntimeException, RuntimeException403;

    List<Meeting> getMeetingPrecedenti(String metaId) throws ServerRuntimeException, RuntimeException403;
}
