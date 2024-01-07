package com.commigo.metaclass.MetaClass.gestionemeeting.service;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.CategoriaRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.MeetingRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.UtenteInMeetingRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("GestioneMeetingService")
@RequiredArgsConstructor
@Slf4j
@Transactional    //ogni operazione è una transazione
public class GestioneMeetingServiceImpl implements GestioneMeetingService{

    private final MeetingRepository meetingRepository;
    private final StanzaRepository stanzaRepository;
    private final UtenteRepository utenteRepository;
    private final UtenteInMeetingRepository utenteInMeetingRepository;
    private final StatoPartecipazioneRepository statoPartecipazioneRepository;

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
   public boolean modificaScheduling(Meeting meeting) throws ServerRuntimeException, RuntimeException403 {
    // Cerca il meeting per verificare se è registrato o meno
      Optional<Meeting> m = meetingRepository.findById(meeting.getId());

      if (m.isPresent()) {
          if (meeting.getStanza().getId() != m.get().getStanza().getId()) {
              throw new RuntimeException403("Messaggio di errore qui...");
          }
          if(meetingRepository.hasOverlappingMeetings(meeting.getInizio(), meeting.getFine())){
              throw new RuntimeException403("il meeting si accavalla con un altro meeting");
          }
          return meetingRepository.updateAttributes(m.get().getId(), meeting)>0;
      } else {
          // Gestisci il caso in cui il meeting non è presente (potrebbe essere opportuno lanciare un'eccezione o fare altro)
          throw new ServerRuntimeException("Meeting non trovato con ID: " + meeting.getId());

      }
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

/**
*
 * @param metaID
 * @param id_meeting
 * @return
 * @throws ServerRuntimeException
 * @throws RuntimeException403
*/
    @Override
    public Boolean accediMeeting(String metaID, Long id_meeting) throws ServerRuntimeException, RuntimeException403 {

        Utente u;
        Meeting m;
        if((u =  utenteRepository.findFirstByMetaId(metaID))==null){
            throw new RuntimeException403("utente non trovato");
        }
        if((m = meetingRepository.findMeetingById(id_meeting))==null){
            throw new RuntimeException403("meeting non trovato");
        }

        //verifica se il meeting e' avviato
        if(!m.isAvviato())    throw new RuntimeException403("Il meeting non e' stato avviato");

        //salvatgagio dell'accesso al meeting
        UtenteInMeeting uim = new UtenteInMeeting(u,m,true);
        try{
             utenteInMeetingRepository.save(uim);
             return true;
        }catch(DataIntegrityViolationException e){
             throw new ServerRuntimeException("errore nel salvataggio dell'utente nel meeting");
        }

    }

    @Override
    public Boolean avviaMeeting(String metaID, Long id_meeting) throws ServerRuntimeException, RuntimeException403 {

        Utente org;
        Meeting m;
        if((org =  utenteRepository.findFirstByMetaId(metaID))==null){
            throw new RuntimeException403("organizzatore non trovato");
        }
        if((m = meetingRepository.findMeetingById(id_meeting))==null){
            throw new RuntimeException403("meeting non trovato");
        }

        //controllo ruolo organizzatore
        //ricerca della stanza
        Stanza s;
        if((s = stanzaRepository.findStanzaById(m.getStanza().getId()))==null){
            throw new ServerRuntimeException("stanza non trovata");
        }
        //ricerca della stato partecipazione
        StatoPartecipazione sp;
        if((sp = statoPartecipazioneRepository
                .findStatoPartecipazioneByUtenteAndStanza(org,s))==null){
            throw new ServerRuntimeException("Errore nella verifica dell'utente in stanza, " +
                    "probabilmente non ha acceduto alla stanza");
        }
        //verifica ruolo
        if(sp.getRuolo().getNome().equalsIgnoreCase("Partecipante")){
            throw new ServerRuntimeException("non puoi avviare il metting. Sei un partecipante");
        }

        //AVVIO MEETING
        m.setAvviato(true);
        if(meetingRepository.updateAttributes(m.getId(), m)==0){
            throw new ServerRuntimeException("errore nell'avvio del meeting");
        }

        //registro l'organizzatore come presente al meeting
        return accediMeeting(metaID, m.getId());

    }

    @Override
    public ResponseEntity<Response<List<Meeting>>> visualizzaSchedulingMeeting(Long idStanza) {


        Stanza s = stanzaRepository.findStanzaById(idStanza);
        if(s == null){
            return ResponseEntity.status(403).body(new Response<>(null,"stanza non trovata"));
        }else{
            List<Meeting> meetings = meetingRepository.findMeetingByStanza(s);
            if(meetings != null) {
                return ResponseEntity.ok(new Response<>(meetings, "Questi sono tutti i meeting schedulati nella stanza selezionata"));
            }else{
                return ResponseEntity.ok(new Response<>(null, "Non ci sono meeting schedulati in questa stanza"));
            }
        }
    }
}
