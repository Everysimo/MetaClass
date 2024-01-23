package com.commigo.metaclass.gestionemeeting.repository;

import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.entity.Stanza;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository del meeting per gestire transazioni con i dati persistenti. */
@Repository("MeetingRepository")
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

  /**
   * Metodo che permette di verificare se un meeting si sovrappone un altro meeting precedentemente
   * schedulato.
   *
   * @param newInizio inizio del meeting che deve essere schedulato.
   * @param newFine fine del meeting che deve essere schedulato.
   * @return valore boolean che identifica la riuscita dell'operazione.
   */
  @Query(
      "SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Meeting m "
          + "WHERE (:newInizio BETWEEN m.inizio AND m.fine OR :newFine BETWEEN m.inizio AND m.fine "
          + "OR m.inizio BETWEEN :newInizio AND :newFine OR m.fine BETWEEN :newInizio AND :newFine "
          + "OR m.inizio <= :newInizio AND m.fine >= :newFine)")
  boolean hasOverlappingMeetings(
      @Param("newInizio") LocalDateTime newInizio, @Param("newFine") LocalDateTime newFine);

  /**
   * Metodo che permette la modifica degli attributi di un meeting.
   *
   * @param meetingId Id del meeting che deve essere modificato.
   * @param attributes Nuovi attributi del meeting.
   * @return un intero che indica il numero di righe modificato.
   */
  @Modifying
  @Transactional
  @Query(
      "UPDATE Meeting m SET "
          + "m.nome = COALESCE(:#{#attributes['nome']}, m.nome), "
          + "m.inizio = COALESCE(:#{#attributes['inizio']}, m.inizio), "
          + "m.fine = COALESCE(:#{#attributes['fine']}, m.fine), "
          + "m.isAvviato= COALESCE(:#{#attributes['isAvviato']}, m.isAvviato) "
          + "WHERE m.id = :meetingId")
  int updateAttributes(
      @Param("meetingId") Long meetingId, @Param("attributes") Map<String, Object> attributes);

  /**
   * Metodo che permette la modifica degli attributi di un meeting.
   *
   * @param meetingId Id del meeting che deve essere modificato.
   * @param attributes Nuovi attributi del meeting.
   * @return un intero che indica il numero di righe modificato.
   */
  @Modifying
  @Transactional
  @Query(
      "UPDATE Meeting m SET "
          + "m.nome = COALESCE(:#{#attributes['nome']}, m.nome), "
          + "m.inizio = COALESCE(:#{#attributes['inizio']}, m.inizio), "
          + "m.fine = COALESCE(:#{#attributes['fine']}, m.fine), "
          + "m.isAvviato= COALESCE(:#{#attributes['isAvviato']}, m.isAvviato) "
          + "WHERE m.id = :meetingId")
  int updateAttributes(@Param("meetingId") Long meetingId, @Param("attributes") Meeting attributes);

  /**
   * Metodo che permette la ricerca di un meeting sulla base di un ID.
   *
   * @param id id su cui è basata la ricerca.
   * @return meeting con id uguale a quello fornito.
   */
  Meeting findMeetingById(Long id);

  /**
   * Metodo che permette la ricerca dei meeting schedulati all'interno di una stanza.
   *
   * @param stanza stanza di cui si vogliono ricerca i meeting schedulati.
   * @return Lista di meeting schedulati all'interno della stanza.
   */
  List<Meeting> findMeetingByStanza(Stanza stanza);
}
