package com.commigo.metaclass.MetaClass.gestionemeeting.service;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;

public interface GestioneMeetingService {
    public boolean creaScheduling(Meeting meeting);
    public Meeting modificaScheduling(Meeting meeting) throws ServerRuntimeException, RuntimeException403;
}
