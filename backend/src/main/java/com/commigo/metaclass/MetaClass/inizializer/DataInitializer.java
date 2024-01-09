package com.commigo.metaclass.MetaClass.inizializer;
import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.CategoriaRepository;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ImmagineRepository;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaServiceImpl;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.multipleid.StatoPartecipazioneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static com.commigo.metaclass.MetaClass.entity.Ruolo.ORGANIZZATORE_MASTER;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RuoloRepository ruoloRepository;
    private final CategoriaRepository categoriaRepository;
    private final ScenarioRepository scenarioRepository;
    private final StanzaRepository stanzaRepository;
    private final UtenteRepository utenteRepository;
    private final StatoPartecipazioneRepository statoPartecipazioneRepository;

    private final ImmagineRepository immagineRepository;

    private final GestioneStanzaServiceImpl stanzaService;



    @Autowired
    public DataInitializer(RuoloRepository ruoloRepository, CategoriaRepository categoriaRepository, ScenarioRepository scenarioRepository, StanzaRepository stanzaRepository, UtenteRepository utenteRepository, StatoPartecipazioneRepository statoPartecipazioneRepository, ImmagineRepository immagineRepository, GestioneStanzaServiceImpl stanzaService) {
        this.ruoloRepository = ruoloRepository;
        this.categoriaRepository = categoriaRepository;
        this.scenarioRepository = scenarioRepository;
        this.stanzaRepository = stanzaRepository;
        this.utenteRepository = utenteRepository;
        this.statoPartecipazioneRepository = statoPartecipazioneRepository;
        this.immagineRepository = immagineRepository;
        this.stanzaService = stanzaService;
    }

    @Override
    public void run(String... args) throws Exception {

        //Aggiunta Utente - Michele è amministratore - Francesco utente semplice
            Utente u1 = utenteRepository.save(new Utente(1L, "Michele", "Pesce", "pescemichele@live.com", "05/30/1993","M","7184488154978627", Utente.DEFAULT_TOKEN, true));
            Utente u2 = utenteRepository.save(new Utente(2L, "Francesco", "Gatto", "francescogatto2001@gmail.com", "11/01/2001","M","7179258205463811", Utente.DEFAULT_TOKEN, false));
            Utente u3 = utenteRepository.save(new Utente(3L, "Giorgio", "Castelluccio", "giorgio_castelluccio@outlook.it", "10/10/2010","M","7168367147841000", Utente.DEFAULT_TOKEN, false));

        //Aggiunta ruolo
            Ruolo r1 = ruoloRepository.save(new Ruolo(1L, "Partecipante"));
            Ruolo r2 = ruoloRepository.save(new Ruolo(2L, "Organizzatore"));
            Ruolo r3 = ruoloRepository.save(new Ruolo(3L, "Organizzatore_Master"));

        //Aggiunta della Categoria
            Categoria c1 = categoriaRepository.save(new Categoria(1L, "Lavoro", "Categoria per il lavoro"));
            Categoria c2 = categoriaRepository.save(new Categoria(2L,"Scuola", "Categoria per la scuola"));
            Categoria c3 = categoriaRepository.save(new Categoria(3L, "Divertimento", "Categoria per il divertimento"));

        //Aggiunta Immagine
            Immagine i1 = immagineRepository.save(new Immagine(1L, "lavoro1.txt","https://www.lavoro1.com/path/to/lavoro1.txt"));
            Immagine i2 = immagineRepository.save(new Immagine(2L, "scuola1.txt","https://www.scuola1.com/path/to/scuola1.txt"));
            Immagine i3 = immagineRepository.save(new Immagine(3L, "divertimento1.txt","https://www.divertimento1.com/path/to/divertimento1.txt"));

        //Aggiunta dello Scenario

            Scenario sc1 = scenarioRepository.save(new Scenario(1L, "Lavoro1", "Scenario 1 per il lavoro", i1, c1));
            Scenario sc2 = scenarioRepository.save(new Scenario(2L, "Scuola1", "Scenario 1 per la scuola", i2, c2));
            Scenario sc3 = scenarioRepository.save(new Scenario(3L, "Divertimento1", "Divertimento 1 per la scuola", i3, c3));


        //Aggiunta della Stanza
            Stanza s1 = stanzaRepository.save(new Stanza(1L, "StanzaLavoro1", "Stanza 1 per il lavoro", true, 500, sc1, "000001"));
            Stanza s2 = stanzaRepository.save(new Stanza(2L, "StanzaScuola1", "Stanza 1 per la scuola", false, 200, sc2, "000002"));
            Stanza s3 = stanzaRepository.save(new Stanza(3L, "StanzaDivertimento1", "Stanza 1 per il divertimento", true, 50, sc3, "000003"));

           //Aggiunta dello StatoPartecipazione
           //SE VIENE INSERITO UN ORGANIZZATORE_MASTER BANNATO ALLORA DA ERRORE
           //STESSA COSA VALE PER isInAttesa SIA PER ORGANIZZATORI CHE ORGANIZZATORI MASTER

            StatoPartecipazione sp1 = statoPartecipazioneRepository.save(new StatoPartecipazione(s1, u1, stanzaService.getRuolo(ORGANIZZATORE_MASTER), false, false, "Michele"));
            StatoPartecipazione sp2 = statoPartecipazioneRepository.save(new StatoPartecipazione(s1, u2, stanzaService.getRuolo(Ruolo.ORGANIZZATORE), false, false, "Francesco"));
            StatoPartecipazione sp3 = statoPartecipazioneRepository.save(new StatoPartecipazione(s1, u3, stanzaService.getRuolo(Ruolo.PARTECIPANTE), true, false, "Giorgio"));

            StatoPartecipazione sp4 = statoPartecipazioneRepository.save(new StatoPartecipazione(s2, u1, stanzaService.getRuolo(Ruolo.PARTECIPANTE), false, true, "Michele"));
            StatoPartecipazione sp5 = statoPartecipazioneRepository.save(new StatoPartecipazione(s2, u2, stanzaService.getRuolo(Ruolo.PARTECIPANTE), false, true, "Francesco"));
            StatoPartecipazione sp6 = statoPartecipazioneRepository.save(new StatoPartecipazione(s3, u3, stanzaService.getRuolo(ORGANIZZATORE_MASTER), false, false, "Giorgio"));

            StatoPartecipazione sp7 = statoPartecipazioneRepository.save(new StatoPartecipazione(s3, u1, stanzaService.getRuolo(Ruolo.PARTECIPANTE), true, false, "Michele"));
            StatoPartecipazione sp8 = statoPartecipazioneRepository.save(new StatoPartecipazione(s3, u2, stanzaService.getRuolo(Ruolo.PARTECIPANTE), false, false, "Francesco"));
            StatoPartecipazione sp9 = statoPartecipazioneRepository.save(new StatoPartecipazione(s3, u3, stanzaService.getRuolo(ORGANIZZATORE_MASTER), false, false, "Giorgio"));

    }

}
