package com.commigo.metaclass.MetaClass.gestionemeeting.repository;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository("MeetingRespository")
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Meeting m " +
            "WHERE (:newInizio BETWEEN m.inizio AND m.fine OR :newFine BETWEEN m.inizio AND m.fine) " +
            "      OR (m.inizio BETWEEN :newInizio AND :newFine OR m.fine BETWEEN :newInizio AND :newFine)")
    boolean hasOverlappingMeetings(@Param("newInizio") LocalDateTime newInizio, @Param("newFine") LocalDateTime newFine);

}
