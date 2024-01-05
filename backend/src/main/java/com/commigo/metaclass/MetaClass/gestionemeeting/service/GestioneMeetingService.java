package com.commigo.metaclass.MetaClass.gestionemeeting.service;

import com.commigo.metaclass.MetaClass.entity.Meeting;

import java.util.Optional;

public interface GestioneMeetingService {
    boolean creaScheduling(Meeting meeting);
    Meeting findMeetingById(Long id);

    Meeting saveMeeting(Meeting meeting);
}
