package com.commigo.metaclass.MetaClass.gestionemeeting.service;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.MeetingRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
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

/**
*
 * @param meeting
 * @return
*/
    @Override
    public boolean creaScheduling(Meeting meeting) {
        try {
            //cerca il meeting per verificare se registrato o meno
            Optional<Meeting> m = meetingRepository.findById(meeting.getId());
            if (!m.isPresent()) {
                // Meeting non presente nel database, lo salva
                meetingRepository.save(meeting);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Stampa la traccia dell'eccezione per debugging
            return false;
        }
    }
}
