package com.commigo.metaclass.MetaClass.gestionemeeting.repository;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("MeetingRespository")
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Meeting m " +
            "WHERE (:newInizio BETWEEN m.inizio AND m.fine OR :newFine BETWEEN m.inizio AND m.fine) " +
            "      OR (m.inizio BETWEEN :newInizio AND :newFine OR m.fine BETWEEN :newInizio AND :newFine)")
    boolean hasOverlappingMeetings(@Param("newInizio") LocalDateTime newInizio, @Param("newFine") LocalDateTime newFine);

    @Modifying
    @Transactional
    @Query("UPDATE Meeting m SET " +
            "m.nome = COALESCE(:#{#attributes['nome']}, m.nome), " +
            "m.inizio = COALESCE(:#{#attributes['inizio']}, m.inizio), " +
            "m.fine = COALESCE(:#{#attributes['fine']}, m.fine), " +
            "m.isAvviato= COALESCE(:#{#attributes['isAvviato']}, m.isAvviato) " +
            "WHERE m.id = :MeetingID")
    int updateAttributes(@Param("MeetingID") Long MeetingID, @Param("attributes") Meeting attributes);

    Meeting findMeetingById(Long id);

    List<Meeting> findMeetingByStanza(Stanza stanza);
}
