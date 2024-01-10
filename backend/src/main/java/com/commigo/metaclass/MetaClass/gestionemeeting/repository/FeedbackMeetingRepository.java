package com.commigo.metaclass.MetaClass.gestionemeeting.repository;

import com.commigo.metaclass.MetaClass.entity.FeedbackMeeting;
import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackMeetingRepository extends JpaRepository<FeedbackMeeting, Long> {
      FeedbackMeeting findFeedbackMeetingByUtenteAndMeeting(Utente utente, Meeting meeting);

      List<FeedbackMeeting> findFeedbackMeetingByUtente(Utente utente);
}
