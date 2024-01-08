package com.commigo.metaclass.MetaClass.gestionemeeting.repository;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.entity.UtenteInMeeting;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UtenteInMeetingRepository extends JpaRepository<UtenteInMeeting, Long> {

    List<UtenteInMeeting> findUtenteInMeetingsByMeeting(Meeting meeting);

    UtenteInMeeting findUtenteInMeetingsByMeetingAndUtente(Meeting meeting, Utente utente);

    @Modifying
    @Transactional
    @Query("UPDATE UtenteInMeeting uim SET " +
            "uim.IsOnline = COALESCE(:#{#attributes['isOnline']}, uim.IsOnline) " +
            "WHERE uim.meeting.id = :MeetingID " +
            "AND uim.utente.id = :UtenteID")
    int updateAttributes(@Param("MeetingID") Long MeetingID, @Param("UtenteID")Long UtenteID,
                         @Param("attributes") UtenteInMeeting attributes);
}
