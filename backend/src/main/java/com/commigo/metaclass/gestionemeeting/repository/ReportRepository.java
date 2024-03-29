package com.commigo.metaclass.gestionemeeting.repository;

import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository del report per gestire transazioni con i dati persistenti. */
public interface ReportRepository extends JpaRepository<Report, Long> {
  /**
   * Metodo che permette la ricerca di un report sulla base di un meeting.
   *
   * @param meeting meeting su cui si basa la ricerca.
   * @return Report del meeting passato alla funzione.
   */
  Report findByMeeting(Meeting meeting);
}
