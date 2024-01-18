package com.commigo.metaclass.MetaClass.gestionemeeting.repository;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
     /**
      * metodo che permette la ricerca di un report sulla base di un meeting
      * @param meeting meeting su cui si basa la ricerca
      * @return Report del meeting passato alla funzione
      */
     Report findByMeeting(Meeting meeting);
}
