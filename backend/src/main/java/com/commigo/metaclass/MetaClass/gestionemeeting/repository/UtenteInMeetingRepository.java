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

    /**
     * metodo che permette di ricerca tutti gli utenti presenti all'interno di un meeting
     * @param meeting meeting su cui si basa la ricerca
     * @return lista di utenti presenti in meeting
     */
    List<UtenteInMeeting> findUtenteInMeetingsByMeeting(Meeting meeting);

    /**
     * Meotodo che permette di ricerca un utente presente all'interno dell'meeting
     * @param meeting metting su cui si basas la ricerca
     * @param utente utente che si vuole ricerca all'interno del meeting
     * @return Utente presente all'interno del meeting
     */
    UtenteInMeeting findUtenteInMeetingsByMeetingAndUtente(Meeting meeting, Utente utente);

    /**
     * metodo che permette di ricercare tutti i meeting in cui è presente un utente
     * @param u utente su cui si basa la ricerca
     * @return lista di UtenteinMeeting
     */
    List<UtenteInMeeting> findUtenteInMeetingsByUtente(Utente u);
}
