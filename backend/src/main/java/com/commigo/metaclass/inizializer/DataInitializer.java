package com.commigo.metaclass.inizializer;

import static com.commigo.metaclass.entity.Ruolo.ORGANIZZATORE_MASTER;

import com.commigo.metaclass.entity.Categoria;
import com.commigo.metaclass.entity.FeedbackMeeting;
import com.commigo.metaclass.entity.Immagine;
import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.entity.Report;
import com.commigo.metaclass.entity.Ruolo;
import com.commigo.metaclass.entity.Scenario;
import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.StatoPartecipazione;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.entity.UtenteInMeeting;
import com.commigo.metaclass.gestioneamministrazione.repository.CategoriaRepository;
import com.commigo.metaclass.gestioneamministrazione.repository.ImmagineRepository;
import com.commigo.metaclass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.gestionemeeting.repository.FeedbackMeetingRepository;
import com.commigo.metaclass.gestionemeeting.repository.MeetingRepository;
import com.commigo.metaclass.gestionemeeting.repository.ReportRepository;
import com.commigo.metaclass.gestionemeeting.repository.UtenteInMeetingRepository;
import com.commigo.metaclass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.gestioneutenza.repository.UtenteRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Classe per popolare il database. */
@Component
public class DataInitializer implements CommandLineRunner {

  private final RuoloRepository ruoloRepository;
  private final CategoriaRepository categoriaRepository;
  private final ScenarioRepository scenarioRepository;
  private final StanzaRepository stanzaRepository;
  private final UtenteRepository utenteRepository;
  private final ReportRepository reportRepository;
  private final FeedbackMeetingRepository feedbackMeetingRepository;
  private final StatoPartecipazioneRepository statoPartecipazioneRepository;

  private final UtenteInMeetingRepository utenteInMeetingRepository;

  private final MeetingRepository meetingRepository;
  private final ImmagineRepository immagineRepository;

  /**
   * Popola il database.
   *
   * @param ruoloRepository repository del ruolo.
   * @param categoriaRepository repository della categoria.
   * @param scenarioRepository repository dello scenario.
   * @param stanzaRepository repository della stanza.
   * @param utenteRepository repository dell'utente.
   * @param reportRepository repository del report.
   * @param feedbackMeetingRepository repository del feedback meeting.
   * @param statoPartecipazioneRepository repository dello stato partecipazione.
   * @param utenteInMeetingRepository repository di utente in meeting.
   * @param meetingRepository repository del meeting.
   * @param immagineRepository repository dell'immagine.
   */
  @Autowired
  public DataInitializer(
      RuoloRepository ruoloRepository,
      CategoriaRepository categoriaRepository,
      ScenarioRepository scenarioRepository,
      StanzaRepository stanzaRepository,
      UtenteRepository utenteRepository,
      ReportRepository reportRepository,
      FeedbackMeetingRepository feedbackMeetingRepository,
      StatoPartecipazioneRepository statoPartecipazioneRepository,
      UtenteInMeetingRepository utenteInMeetingRepository,
      MeetingRepository meetingRepository,
      ImmagineRepository immagineRepository) {
    this.ruoloRepository = ruoloRepository;
    this.categoriaRepository = categoriaRepository;
    this.scenarioRepository = scenarioRepository;
    this.stanzaRepository = stanzaRepository;
    this.utenteRepository = utenteRepository;
    this.reportRepository = reportRepository;
    this.feedbackMeetingRepository = feedbackMeetingRepository;
    this.statoPartecipazioneRepository = statoPartecipazioneRepository;
    this.utenteInMeetingRepository = utenteInMeetingRepository;
    this.meetingRepository = meetingRepository;
    this.immagineRepository = immagineRepository;
  }

  @Override
  public void run(String... args) throws Exception {

    // Aggiunta Utente - Michele è amministratore - Francesco utente semplice
    final Utente u1 =
        utenteRepository.save(
            new Utente(
                1L,
                "Michele",
                "Pesce",
                "pescemichele@live.com",
                "05/30/1993",
                "M",
                "7184488154978627",
                Utente.DEFAULT_TOKEN,
                true));
    final Utente u2 =
        utenteRepository.save(
            new Utente(
                2L,
                "Francesco",
                "Gatto",
                "francescogatto2001@gmail.com",
                "11/01/2001",
                "M",
                "7179258205463811",
                Utente.DEFAULT_TOKEN,
                false));
    final Utente u3 =
        utenteRepository.save(
            new Utente(
                3L,
                "Giorgio",
                "Castelluccio",
                "giorgio_castelluccio@outlook.it",
                "10/10/2010",
                "M",
                "7168367147841000",
                Utente.DEFAULT_TOKEN,
                false));
    final Utente u4 =
        utenteRepository.save(
            new Utente(
                4L,
                "Domenico",
                "Cavaliere",
                "d.cavaliere13@studenti.unisa.it",
                "01/16/2002",
                "M",
                "2564108403764347",
                Utente.DEFAULT_TOKEN,
                true));
    final Utente u5 =
        utenteRepository.save(
            new Utente(
                5L,
                "Carmine",
                "Detta",
                "carminedetta02@gmail.com",
                "08/11/2002",
                "M",
                "725841336137765",
                Utente.DEFAULT_TOKEN,
                true));

    // Aggiunta ruolo
    ruoloRepository.save(new Ruolo(1L, "Partecipante"));
    ruoloRepository.save(new Ruolo(2L, "Organizzatore"));
    ruoloRepository.save(new Ruolo(3L, "Organizzatore_Master"));

    // Aggiunta della Categoria
    Categoria c1 = categoriaRepository.save(new Categoria(1L, "Lavoro", "Categoria per il lavoro"));
    Categoria c2 = categoriaRepository.save(new Categoria(2L, "Scuola", "Categoria per la scuola"));
    Categoria c3 =
        categoriaRepository.save(
            new Categoria(3L, "Divertimento", "Categoria per il divertimento"));

    // Aggiunta Immagine
    Immagine i1 =
        immagineRepository.save(
            new Immagine(1L, "lavoro1.txt", "https://www.lavoro1.com/path/to/lavoro1.txt"));
    Immagine i2 =
        immagineRepository.save(
            new Immagine(2L, "scuola1.txt", "https://www.scuola1.com/path/to/scuola1.txt"));
    Immagine i3 =
        immagineRepository.save(
            new Immagine(
                3L,
                "divertimento1.txt",
                "https://www.divertimento1.com/path/to/divertimento1.txt"));

    // Aggiunta dello Scenario

    Scenario sc1 =
        scenarioRepository.save(new Scenario(1L, "Lavoro1", "Scenario 1 per il lavoro", i1, c1));
    Scenario sc2 =
        scenarioRepository.save(new Scenario(2L, "Scuola1", "Scenario 1 per la scuola", i2, c2));
    Scenario sc3 =
        scenarioRepository.save(
            new Scenario(3L, "Divertimento1", "Divertimento 1 per la scuola", i3, c3));

    // Aggiunta della Stanza
    Stanza s1 =
        stanzaRepository.save(
            new Stanza(1L, "StanzaLavoro1", "Stanza 1 per il lavoro", true, 500, sc1, "000001"));
    Stanza s2 =
        stanzaRepository.save(
            new Stanza(2L, "StanzaScuola1", "Stanza 1 per la scuola", false, 200, sc2, "000002"));
    Stanza s3 =
        stanzaRepository.save(
            new Stanza(
                3L,
                "StanzaDivertimento1",
                "Stanza 1 per il divertimento",
                true,
                50,
                sc3,
                "000003"));
    Stanza s4 =
        stanzaRepository.save(
            new Stanza(4L, "StanzaCarmine1", "Stanza 1 per il Carmine", true, 50, sc3, "000004"));
    Stanza s5 =
        stanzaRepository.save(
            new Stanza(5L, "StanzaDomenico1", "Stanza 1 per il domenico", true, 50, sc3, "000005"));

    // Aggiunta dello StatoPartecipazione
    // SE VIENE INSERITO UN ORGANIZZATORE_MASTER BANNATO ALLORA DA ERRORE
    // STESSA COSA VALE PER isInAttesa SIA PER ORGANIZZATORI CHE ORGANIZZATORI MASTER

    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s1,
            u1,
            ruoloRepository.findByNome(Ruolo.ORGANIZZATORE),
            false,
            false,
            "Michele",
            false));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s1,
            u2,
            ruoloRepository.findByNome(Ruolo.ORGANIZZATORE_MASTER),
            false,
            false,
            "Francesco",
            true));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s1, u3, ruoloRepository.findByNome(Ruolo.PARTECIPANTE), true, false, "Giorgio", true));

    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s2,
            u1,
            ruoloRepository.findByNome(Ruolo.PARTECIPANTE),
            false,
            false,
            "Michele",
            false));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s2,
            u2,
            ruoloRepository.findByNome(Ruolo.PARTECIPANTE),
            false,
            false,
            "Francesco",
            false));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s3,
            u3,
            ruoloRepository.findByNome(ORGANIZZATORE_MASTER),
            false,
            false,
            "Giorgio",
            false));

    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s3, u1, ruoloRepository.findByNome(Ruolo.PARTECIPANTE), true, false, "Michele", false));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s3,
            u2,
            ruoloRepository.findByNome(Ruolo.PARTECIPANTE),
            false,
            false,
            "Francesco",
            true));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s3,
            u3,
            ruoloRepository.findByNome(ORGANIZZATORE_MASTER),
            false,
            false,
            "Giorgio",
            true));

    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s4,
            u5,
            ruoloRepository.findByNome(ORGANIZZATORE_MASTER),
            false,
            false,
            "Carmine",
            false));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s4,
            u1,
            ruoloRepository.findByNome(Ruolo.ORGANIZZATORE),
            false,
            false,
            "Michele",
            false));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s4,
            u2,
            ruoloRepository.findByNome(Ruolo.PARTECIPANTE),
            true,
            false,
            "Francesco",
            true));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s4, u3, ruoloRepository.findByNome(Ruolo.PARTECIPANTE), false, true, "Giorgio", false));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s4,
            u4,
            ruoloRepository.findByNome(Ruolo.PARTECIPANTE),
            false,
            false,
            "Domenico",
            false));

    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s5,
            u1,
            ruoloRepository.findByNome(Ruolo.ORGANIZZATORE),
            false,
            false,
            "Michele",
            false));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s5,
            u2,
            ruoloRepository.findByNome(Ruolo.PARTECIPANTE),
            true,
            false,
            "Francesco",
            true));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s5,
            u3,
            ruoloRepository.findByNome(Ruolo.PARTECIPANTE),
            false,
            false,
            "Giorgio",
            false));
    statoPartecipazioneRepository.save(
        new StatoPartecipazione(
            s5,
            u4,
            ruoloRepository.findByNome(ORGANIZZATORE_MASTER),
            false,
            false,
            "Domenico",
            false));
    statoPartecipazioneRepository.save(
            new StatoPartecipazione(
                    s5,
                    u5,
                    ruoloRepository.findByNome(Ruolo.PARTECIPANTE),
                    false,
                    false,
                    "Carmine",
                    false));

    // Aggiunta meeting
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    Meeting m1 =
        meetingRepository.save(
            new Meeting(
                1L,
                "MeetingStanza4",
                LocalDateTime.parse("2024-02-02 18:00", formatter),
                LocalDateTime.parse("2024-02-02 20:00", formatter),
                false,
                sc1,
                s4));

    Meeting m2 =
        meetingRepository.save(
            new Meeting(
                2L,
                "MeetingStanza4",
                LocalDateTime.parse("2024-02-03 18:00", formatter),
                LocalDateTime.parse("2024-02-03 20:00", formatter),
                false,
                sc1,
                s4));
    Meeting m3 =
        meetingRepository.save(
            new Meeting(
                3L,
                "MeetingStanza5",
                LocalDateTime.parse("2024-02-03 18:00", formatter),
                LocalDateTime.parse("2024-02-03 20:00", formatter),
                false,
                sc1,
                s5));

    // Aggiunta Utente in meeting
    utenteInMeetingRepository.save(new UtenteInMeeting(u5, m1, true));
    utenteInMeetingRepository.save(new UtenteInMeeting(u4, m1, true));
    utenteInMeetingRepository.save(new UtenteInMeeting(u2, m1, true));
    utenteInMeetingRepository.save(new UtenteInMeeting(u1, m1, true));

    utenteInMeetingRepository.save(new UtenteInMeeting(u5, m2, false));
    utenteInMeetingRepository.save(new UtenteInMeeting(u2, m2, false));
    utenteInMeetingRepository.save(new UtenteInMeeting(u3, m2, false));

    // aggiunta repoort
    List<Utente> usersRep1 = new ArrayList<>();
    usersRep1.add(u1);
    usersRep1.add(u2);
    usersRep1.add(u4);
    usersRep1.add(u5);
    final Report rep1 =
        reportRepository.save(new Report(1L, 4, Duration.ofHours(1), 4, m1, usersRep1));
    List<Utente> usersRep2 = new ArrayList<>();
    usersRep2.add(u2);
    usersRep2.add(u3);
    usersRep2.add(u5);
    Report rep2 = reportRepository.save(new Report(2L, 3, Duration.ofHours(1), 3, m2, usersRep2));

    // aggiunta feedackmeeting
    feedbackMeetingRepository.save(
        new FeedbackMeeting(
            u1,
            m1,
            rep1,
            Duration.ofMinutes(30),
            LocalDateTime.parse("2024-02-03 19:00", formatter),
            false));
    feedbackMeetingRepository.save(
        new FeedbackMeeting(
            u2,
            m1,
            rep1,
            Duration.ofMinutes(30),
            LocalDateTime.parse("2024-02-03 19:00", formatter),
            false));
    feedbackMeetingRepository.save(
        new FeedbackMeeting(
            u4,
            m1,
            rep1,
            Duration.ofMinutes(30),
            LocalDateTime.parse("2024-02-03 19:00", formatter),
            false));
    feedbackMeetingRepository.save(
        new FeedbackMeeting(
            u5,
            m1,
            rep1,
            Duration.ofMinutes(30),
            LocalDateTime.parse("2024-02-03 19:00", formatter),
            false));
    feedbackMeetingRepository.save(
        new FeedbackMeeting(
            u2,
            m2,
            rep2,
            Duration.ofMinutes(30),
            LocalDateTime.parse("2024-02-03 19:00", formatter),
            false));
    feedbackMeetingRepository.save(
        new FeedbackMeeting(
            u3,
            m2,
            rep2,
            Duration.ofMinutes(30),
            LocalDateTime.parse("2024-02-03 19:00", formatter),
            true));
    feedbackMeetingRepository.save(
        new FeedbackMeeting(
            u5,
            m2,
            rep2,
            Duration.ofMinutes(30),
            LocalDateTime.parse("2024-02-03 19:00", formatter),
            false));
  }
}
