package com.commigo.metaclass.MetaClass.gestionemeeting.service;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.MeetingRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("GestioneMeetingService")
@RequiredArgsConstructor
@Slf4j
@Transactional    //ogni operazione è una transazione
public class GestioneMeetingServiceImpl implements GestioneMeetingService{

    private final MeetingRepository meetingRepository;
    private final StanzaRepository stanzaRepository;

/**
*
 * @param meeting
 * @return
*/
    @Override
    public boolean creaScheduling(Meeting meeting) throws RuntimeException{
            //cerca il meeting per verificare se registrato o meno
            Optional<Meeting> m = meetingRepository.findById(meeting.getId());
            if (!m.isPresent()) {

                //ricerca della stanza da associare a meeting
                Stanza s = stanzaRepository.findStanzaById(meeting.getStanza().getId());
                if(s==null){
                    throw new RuntimeException("nessuna stanza ha quell'id");
                }
                if(meetingRepository.hasOverlappingMeetings(meeting.getInizio(), meeting.getFine())){
                    throw new RuntimeException("il meeting si accavalla con un altro meeting");
                }
                meeting.setStanza(s);

                //salvo lo scenario iniziale
                meeting.setScenario_iniziale(s.getScenario());

                // Meeting non presente nel database, lo salva
                meetingRepository.save(meeting);
            }
            return true;
    }

/**
*
 * @param meeting
 * @return
*/
   @Override
   public Meeting modificaScheduling(Meeting meeting) throws ServerRuntimeException, RuntimeException403 {
    // Cerca il meeting per verificare se è registrato o meno
      Optional<Meeting> m = meetingRepository.findById(meeting.getId());

      if (m.isPresent()) {
          if (meeting.getStanza().getId() != m.get().getStanza().getId()) {
              throw new RuntimeException403("Messaggio di errore qui...");
          }
          if(meetingRepository.updateAttributes(m.get().getId(), meeting)>0){
              return meetingRepository.findMeetingById(m.get().getId());
          }
      } else {
          // Gestisci il caso in cui il meeting non è presente (potrebbe essere opportuno lanciare un'eccezione o fare altro)
          throw new ServerRuntimeException("Meeting non trovato con ID: " + meeting.getId());

      }

      return null;
   }

    @Override
    public Meeting findMeetingById(Long id) {
        if(meetingRepository.findById(id).isPresent())
        {
            return meetingRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public Meeting saveMeeting(Meeting meeting)
    {
        return meetingRepository.save(meeting);
    }
}
