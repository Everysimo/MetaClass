package com.commigo.metaclass.gestionemeeting.repository;

import com.commigo.metaclass.entity.FeedbackMeeting;
import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.entity.Utente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository del meeting per gestire transazioni con i dati persistenti. */
public interface FeedbackMeetingRepository extends JpaRepository<FeedbackMeeting, Long> {
  /**
   * Metodo che permette di ricercare il questionario compilato da un determinato utente che fa
   * riferimento a un determinato meeting.
   *
   * @param utente Utente su cui si basa la ricerca.
   * @param meeting meeting su cui si basa la ricerca.
   * @return Questionario compilato dell'utente.
   */
  FeedbackMeeting findFeedbackMeetingByUtenteAndMeeting(Utente utente, Meeting meeting);

  /**
   * Metodo che permette di ricercare tutti i questionari compilati da un determinato utente.
   *
   * @param utente utente su cui si basa la ricerca.
   * @return lista di feedback meeting dato un utente.
   */
  List<FeedbackMeeting> findFeedbackMeetingByUtente(Utente utente);
}
