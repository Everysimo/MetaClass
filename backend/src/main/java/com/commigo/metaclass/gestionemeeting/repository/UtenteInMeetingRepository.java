package com.commigo.metaclass.gestionemeeting.repository;

import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.entity.UtenteInMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtenteInMeetingRepository extends JpaRepository<UtenteInMeeting, Long> {

  /**
   * metodo che permette di ricerca tutti gli utenti presenti all'interno di un meeting
   *
   * @param meeting meeting su cui si basa la ricerca
   * @return lista di utenti presenti in meeting
   */
  List<UtenteInMeeting> findUtenteInMeetingsByMeeting(Meeting meeting);

  /**
   * Meotodo che permette di ricerca un utente presente all'interno dell'meeting
   *
   * @param meeting metting su cui si basas la ricerca
   * @param utente utente che si vuole ricerca all'interno del meeting
   * @return Utente presente all'interno del meeting
   */
  UtenteInMeeting findUtenteInMeetingsByMeetingAndUtente(Meeting meeting, Utente utente);

  /**
   * metodo che permette di ricercare tutti i meeting in cui è presente un utente
   *
   * @param u utente su cui si basa la ricerca
   * @return lista di UtenteinMeeting
   */
  List<UtenteInMeeting> findUtenteInMeetingsByUtente(Utente u);
}
