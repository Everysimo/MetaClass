package com.commigo.metaclass.MetaClass.gestionemeeting.service;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;

import java.util.Optional;

public interface GestioneMeetingService {
    public boolean creaScheduling(Meeting meeting);
    public boolean modificaScheduling(Meeting meeting) throws ServerRuntimeException, RuntimeException403;;
    Meeting findMeetingById(Long id);
    Meeting saveMeeting(Meeting meeting);
}
