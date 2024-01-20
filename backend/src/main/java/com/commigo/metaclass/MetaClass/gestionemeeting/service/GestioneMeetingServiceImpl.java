package com.commigo.metaclass.MetaClass.gestionemeeting.service;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.FeedbackMeetingRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.MeetingRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.ReportRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.UtenteInMeetingRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestionestimaduratameeting.service.GestioneStimaMeetingService;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional // ogni operazione è una transazione
public class GestioneMeetingServiceImpl implements GestioneMeetingService {

  private final MeetingRepository meetingRepository;
  private final StanzaRepository stanzaRepository;
  private final UtenteRepository utenteRepository;
  private final ScenarioRepository scenarioRepository;
  private final UtenteInMeetingRepository utenteInMeetingRepository;
  private final StatoPartecipazioneRepository statoPartecipazioneRepository;
  private final FeedbackMeetingRepository feedbackMeetingRepository;
  private final ReportRepository reportRepository;

  @Autowired GestioneStimaMeetingService gestioneStimaMeetingService;

  /**
   * Metodo che permette la schedulazione di un meeting
   *
   * @param meeting meeting che si vuole schedulare
   * @param metaID metaID dell'utente che vuole schedulare il meeting
   * @return valore booleano che identifica il successo dell'operazione
   * @throws ServerRuntimeException
   * @throws RuntimeException403
   */
  @Override
  public boolean creaScheduling(Meeting meeting, String metaID)
      throws ServerRuntimeException, RuntimeException403 {
    // cerca il meeting per verificare se registrato o meno
    Optional<Meeting> m = meetingRepository.findById(meeting.getId());
    if (m.isEmpty()) {

      // ricerca della stanza da associare a meeting
      Stanza s = stanzaRepository.findStanzaById(meeting.getStanza().getId());
      if (s == null) {
        throw new ServerRuntimeException("errore nella ricerca della stanza");
      }

      // controllo esistenza dell'utente
      Utente u;
      if ((u = utenteRepository.findFirstByMetaId(metaID)) == null) {
        throw new RuntimeException403("utente non trovato");
      }

      // controllo del ruolo di organizzatore o organizzatore master
      StatoPartecipazione sp;
      if ((sp = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, s))
          == null) {
        throw new ServerRuntimeException("errore nella ricerca del ruolo");
      }
      if (sp.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)) {
        throw new RuntimeException403("non hai i permessi per schedulare un meeting");
      }

      // controllo dei meeting sovrapponibili
      if (meetingRepository.hasOverlappingMeetings(meeting.getInizio(), meeting.getFine())) {
        throw new RuntimeException403("il meeting si accavalla con un altro meeting");
      }
      meeting.setStanza(s);

      // salvo lo scenario iniziale
      meeting.setScenario_iniziale(s.getScenario());

      // Meeting non presente nel database, lo salva
      meetingRepository.save(meeting);
    } else {
      throw new ServerRuntimeException("meeting già schedulato in precedenza");
    }

    return true;
  }

  /**
   * Metodo che permette la modifica di un meeting precedentemente schedulato
   *
   * @param meeting Meeting di cui si vuole modificare la schedulazione
   * @return valore boolean che identifica il successo dell'operazione
   * @throws ServerRuntimeException
   * @throws RuntimeException403
   */
  @Override
  public boolean modificaScheduling(Meeting meeting)
      throws ServerRuntimeException, RuntimeException403 {
    // Cerca il meeting per verificare se è registrato o meno
    Optional<Meeting> m = meetingRepository.findById(meeting.getId());

    if (m.isPresent()) {
      if (meeting.getStanza().getId() != m.get().getStanza().getId()) {
        throw new RuntimeException403("Messaggio di errore qui...");
      }
      if (meetingRepository.hasOverlappingMeetings(meeting.getInizio(), meeting.getFine())) {
        throw new RuntimeException403("il meeting si accavalla con un altro meeting");
      }
      return meetingRepository.updateAttributes(m.get().getId(), meeting) > 0;
    } else {
      // Gestisci il caso in cui il meeting non è presente (potrebbe essere opportuno lanciare
      // un'eccezione o fare altro)
      throw new ServerRuntimeException("Meeting non trovato con ID: " + meeting.getId());
    }
  }

  /**
   * Metodo che permette di fare accesso ad un meeting
   *
   * @param metaID metaID dell'utente che vuole effettuare l'accesso all'interno del meeting
   * @param id_meeting id del meeting a cui si vuole fare accesso
   * @return valore booleano che identifica il successo dell'operazione
   * @throws ServerRuntimeException
   * @throws RuntimeException403
   */
  @Override
  public Boolean accediMeeting(String metaID, Long id_meeting)
      throws ServerRuntimeException, RuntimeException403 {

    Utente u;
    Meeting m;
    if ((u = utenteRepository.findFirstByMetaId(metaID)) == null) {
      throw new RuntimeException403("utente non trovato");
    }
    if ((m = meetingRepository.findMeetingById(id_meeting)) == null) {
      throw new RuntimeException403("meeting non trovato");
    }

    // verifica se il meeting e' avviato
    if (!m.isAvviato()) throw new RuntimeException403("Il meeting non e' stato avviato");

    // salvataggio dell'accesso al meeting
    UtenteInMeeting uim = new UtenteInMeeting(u, m, true);
    try {

      // verifico se l'utente ha era già entrato in precedenza
      UtenteInMeeting existingUim;
      if ((existingUim = utenteInMeetingRepository.findUtenteInMeetingsByMeetingAndUtente(m, u))
          != null) {
        if (!existingUim.isIsOnline()) { // per poi verificare se è già presente

          // se era uscito e sta rieffettuando l'accesso allora bisogna aggiornare
          // le sue azioni in FeedBackMeeting
          FeedbackMeeting fm =
              feedbackMeetingRepository.findFeedbackMeetingByUtenteAndMeeting(u, m);

          // agiornamento di dataUltimoAccesso
          fm.setDataUltimoAccesso(LocalDateTime.now());
          feedbackMeetingRepository.save(fm);

        } else {
          throw new RuntimeException403("già sei all'interno del meeting");
        }
      } else {
        utenteInMeetingRepository.save(uim);

        // creazione del feedback meeting
        Report rep = reportRepository.findByMeeting(m);
        FeedbackMeeting fm = new FeedbackMeeting(u, m, rep);
        feedbackMeetingRepository.save(fm);
      }

      return true;
    } catch (DataIntegrityViolationException e) {
      throw new ServerRuntimeException("errore nel salvataggio dei dati nel database");
    }
  }

  /**
   * metodo che permette di avviare un meeting
   *
   * @param metaID metaID dell'utente che vuole avviare il meeting
   * @param id_meeting id del meeting che deve essere avviato
   * @return valore booleano che identifica il successo dell'operazione
   * @throws ServerRuntimeException
   * @throws RuntimeException403
   */
  @Override
  public Boolean avviaMeeting(String metaID, Long id_meeting)
      throws ServerRuntimeException, RuntimeException403 {

    Utente org;
    Meeting m;
    if ((org = utenteRepository.findFirstByMetaId(metaID)) == null) {
      throw new RuntimeException403("organizzatore non trovato");
    }
    if ((m = meetingRepository.findMeetingById(id_meeting)) == null) {
      throw new RuntimeException403("meeting non trovato");
    }

    // controllo ruolo organizzatore
    // ricerca della stanza
    Stanza s;
    if ((s = stanzaRepository.findStanzaById(m.getStanza().getId())) == null) {
      throw new ServerRuntimeException("stanza non trovata");
    }
    // ricerca della stato partecipazione
    StatoPartecipazione sp;
    if ((sp = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(org, s))
        == null) {
      throw new ServerRuntimeException(
          "Errore nella verifica dell'utente in stanza, "
              + "probabilmente non ha acceduto alla stanza");
    }
    // verifica ruolo
    if (sp.getRuolo().getNome().equalsIgnoreCase("Partecipante")) {
      throw new ServerRuntimeException("non puoi avviare il metting. Sei un partecipante");
    }

    // AVVIO MEETING
    m.setAvviato(true);
    if (meetingRepository.updateAttributes(m.getId(), m) == 0) {
      throw new ServerRuntimeException("errore nell'avvio del meeting");
    }

    try {
      // creazione del report che sarà aggiornato a fine meeting
      Report report = new Report(m, org);
      reportRepository.save(report);

      // registro l'organizzatore come presente al meeting
      return accediMeeting(metaID, m.getId());

    } catch (DataIntegrityViolationException e) {
      throw new ServerRuntimeException("errore nel salvataggio del report");
    }
  }

  /**
   * metodo che permette di terminare un meeting precedentemente avviato
   *
   * @param metaID metaID
   * @param id_meeting
   * @return
   * @throws ServerRuntimeException
   * @throws RuntimeException403
   */
  @Override
  public Boolean terminaMeeting(String metaID, Long id_meeting)
      throws ServerRuntimeException, RuntimeException403 {

    Utente org;
    Meeting m;
    if ((org = utenteRepository.findFirstByMetaId(metaID)) == null) {
      throw new RuntimeException403("organizzatore non trovato");
    }
    if ((m = meetingRepository.findMeetingById(id_meeting)) == null) {
      throw new RuntimeException403("meeting non trovato");
    }

    // controllo ruolo organizzatore
    // ricerca della stanza
    Stanza s;
    if ((s = stanzaRepository.findStanzaById(m.getStanza().getId())) == null) {
      throw new ServerRuntimeException("stanza non trovata");
    }
    // ricerca della stato partecipazione
    StatoPartecipazione sp;
    if ((sp = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(org, s))
        == null) {
      throw new ServerRuntimeException(
          "Errore nella verifica dell'utente in stanza, "
              + "probabilmente non ha acceduto alla stanza");
    }
    // verifica ruolo
    if (sp.getRuolo().getNome().equalsIgnoreCase("Partecipante")) {
      throw new ServerRuntimeException("non puoi terminare il metting. Sei un partecipante");
    }

    // TERMINAZIONE MEETING
    m.setAvviato(false);
    if (meetingRepository.updateAttributes(m.getId(), m) == 0) {
      throw new ServerRuntimeException(
          "errore nella terminazione del meeting, " + "l'entità meeting non si è aggiornata");
    }

    // Prelevo la lista di utenti presenti nel meeting
    List<UtenteInMeeting> uimList = utenteInMeetingRepository.findUtenteInMeetingsByMeeting(m);

    // richiamo la funzione uscitaMeeting per far uscire ogni utente
    int count = 0;
    for (UtenteInMeeting uim : uimList) {
      if (uim.isIsOnline()) {
        uscitaMeeting(uim.getUtente().getMetaId(), m.getId());
        count++;
      }
    }

    // aggiorno il report ai nuovi dati sul meeting terminato
    Report rep = reportRepository.findByMeeting(m);
    rep.setNum_Partecipanti(count);
    rep.setMax_Partecipanti(uimList.size());

    // calcolo tempo trascorso del meeting
    Duration tempoTrascorso = Duration.between(rep.getData_Creazione(), LocalDateTime.now());
    rep.setDurataMeeting(tempoTrascorso);

    // prelevo la lista di utenti
    List<Utente> utentiList =
        uimList.stream().map(uim -> uim.getUtente()).collect(Collectors.toList());

    rep.setLista_partecipanti(utentiList);

    try {
      reportRepository.save(rep);
      return true;
    } catch (DataIntegrityViolationException e) {
      throw new ServerRuntimeException("errore nel salvataggio del report");
    }
  }

  /**
   * metodo che consente ad un utente di uscire da un meeting
   *
   * @param metaID metaID dell'utente che deve uscire dal meeting
   * @param id_meeting id del meeting da cui l'utente ha intenzione di uscire
   * @return valore boolean che identifica l'esito dell'operazione
   * @throws ServerRuntimeException
   * @throws RuntimeException403
   */
  @Override
  public Boolean uscitaMeeting(String metaID, Long id_meeting)
      throws ServerRuntimeException, RuntimeException403 {
    Utente u;
    Meeting m;
    if ((u = utenteRepository.findFirstByMetaId(metaID)) == null) {
      throw new RuntimeException403("organizzatore non trovato");
    }
    if ((m = meetingRepository.findMeetingById(id_meeting)) == null) {
      throw new RuntimeException403("meeting non trovato");
    }

    try {
      // USCITA DAL MEETING
      // si aggiorna in UtenteInMeeting il campo isOnline
      UtenteInMeeting uim = utenteInMeetingRepository.findUtenteInMeetingsByMeetingAndUtente(m, u);
      uim.setIsOnline(false);
      utenteInMeetingRepository.save(uim);

      // Aggiornamento della durata totale del meeting
      FeedbackMeeting fm = feedbackMeetingRepository.findFeedbackMeetingByUtenteAndMeeting(u, m);

      // Calcolo la differenza tra DataPrimoAccesso e LocalDateTime.now()
      Duration tempoTrascorso = Duration.between(fm.getDataPrimoAccesso(), LocalDateTime.now());
      fm.setTempo_totale(tempoTrascorso);
      feedbackMeetingRepository.save(fm);

      return true;

    } catch (DataIntegrityViolationException e) {
      throw new ServerRuntimeException("errore nel salvataggio dei dati nel database");
    }
  }

  /**
   * metodo che consente di visualizzare la lista dei meeting schedulati all'interno della stanza
   *
   * @param idStanza id della stanza di cui vogliamo visualizzare i meeting schedulati
   * @return lista di meeting schedulati all'interno della stanza selezionata
   */
  @Override
  public ResponseEntity<Response<List<Meeting>>> visualizzaSchedulingMeeting(Long idStanza) {

    Stanza s = stanzaRepository.findStanzaById(idStanza);
    if (s == null) {
      return ResponseEntity.status(403).body(new Response<>(null, "stanza non trovata"));
    } else {
      List<Meeting> meetings = meetingRepository.findMeetingByStanza(s);
      if (meetings != null) {
        return ResponseEntity.ok(
            new Response<>(
                meetings, "Questi sono tutti i meeting schedulati nella stanza selezionata"));
      } else {
        return ResponseEntity.ok(
            new Response<>(null, "Non ci sono meeting schedulati in questa stanza"));
      }
    }
  }

  /**
   * metodo che consente di visualizzare i questionari di un determinato utente
   *
   * @param metaId metaId dell'utente che deve visualizzare i meeting
   * @return lista di meeting di cui l'utente possiede un questionari
   * @throws ServerRuntimeException
   * @throws RuntimeException403
   */
  @Override
  public List<Meeting> visualizzaQuestionari(String metaId)
      throws ServerRuntimeException, RuntimeException403 {

    Utente u;
    if ((u = utenteRepository.findFirstByMetaId(metaId)) == null)
      throw new ServerRuntimeException("errore nella ricerca dell'utente");

    List<FeedbackMeeting> feedbacks = feedbackMeetingRepository.findFeedbackMeetingByUtente(u);
    if (feedbacks == null)
      throw new RuntimeException403("non hai partecipato ancora a nessun meeting");

    return feedbacks.stream()
        .filter(feedbackMeeting -> !feedbackMeeting.isCompiledQuestionario())
        .map(FeedbackMeeting::getMeeting)
        .filter(meeting -> !meeting.isAvviato())
        .collect(Collectors.toList());
  }

  /**
   * metodo che consente la visualizzazione di tutti i meeting a cui ha già partecipato un
   * determinato utente
   *
   * @param metaId metaID dell'utente che ha partecipato ai meeting
   * @return lista di tutti i meeting a cui l'utente ha già partecipato
   * @throws ServerRuntimeException
   * @throws RuntimeException403
   */
  @Override
  public List<Meeting> getMeetingPrecedenti(String metaId)
      throws ServerRuntimeException, RuntimeException403 {

    Utente u;
    if ((u = utenteRepository.findFirstByMetaId(metaId)) == null)
      throw new ServerRuntimeException("errore nella ricerca dell'utente");

    List<UtenteInMeeting> uim = utenteInMeetingRepository.findUtenteInMeetingsByUtente(u);
    if (uim == null) throw new RuntimeException403("non hai partecipato ancora a nessun meeting");

    return uim.stream()
        .map(UtenteInMeeting::getMeeting)
        .filter(meeting -> !meeting.isAvviato())
        .collect(Collectors.toList());
  }

  /**
   * metodo che consente la compilazione di un questionario da parte di un determinato utente
   *
   * @param value la valutazione dello scenario inserita dall'utente all'interno del questionario
   * @param motionSickness livello di nausea dopo l'uscita dal meeting
   * @param metaId metaID dell'utente che compila il questionario
   * @param id_meeting id del meeting a cui fa riferimento il questionario
   * @return valore boolean che identifica l'esito dell'operazione
   * @throws ServerRuntimeException
   * @throws RuntimeException403
   */
  @Override
  public boolean compilaQuestionario(
      Integer value, Integer motionSickness, String metaId, Long id_meeting)
      throws ServerRuntimeException, RuntimeException403 {

    Utente u;
    if ((u = utenteRepository.findFirstByMetaId(metaId)) == null)
      throw new ServerRuntimeException("errore nella ricerca dell'utente");

    Meeting m;
    if ((m = meetingRepository.findMeetingById(id_meeting)) == null) {
      throw new RuntimeException403("meeting non trovato");
    }

    // controllo se esiste il feedbackmeeting
    FeedbackMeeting fm = feedbackMeetingRepository.findFeedbackMeetingByUtenteAndMeeting(u, m);
    if (fm == null) throw new ServerRuntimeException("errore nella ricerca del feedback meeting");

    // controllo se il questionario già è stato compilato
    if (fm.isCompiledQuestionario()) throw new RuntimeException403("questionario già compilato");

    // determino che il questionario è salvato
    fm.setCompiledQuestionario(true);
    // aggiungo il livello di immersività e il motionSickness
    fm.setImmersionLevel(value);
    fm.setMotionSickness(motionSickness);

    feedbackMeetingRepository.save(fm);

    // se non è compilato allora procedo con la compilazione
    Scenario sc = m.getScenario_iniziale();
    if (sc == null) throw new ServerRuntimeException("errore nella ricerca dello scenario");

    // prelevo media e numero dei voti
    float media = sc.getMedia_valutazione();
    int num_voti = sc.getNum_voti();

    // calcolo media
    float newMedia = ((media * num_voti) + value) / (num_voti + 1);

    // inserimento della media nello scenario
    sc.setMedia_valutazione(newMedia);
    sc.setNum_voti(num_voti + 1);
    scenarioRepository.save(sc);

    return true;
  }
}
