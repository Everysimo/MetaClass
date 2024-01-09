package com.commigo.metaclass.MetaClass.inizializer;
import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.CategoriaRepository;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ImmagineRepository;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaServiceImpl;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
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
        if (!utenteRepository.existsById((long)1)) {
            utenteRepository.save(new Utente("Michele", "Pesce", "pescemichele@live.com", "05/30/1993","M","7184488154978627", Utente.DEFAULT_TOKEN, true));
        }

        if (!utenteRepository.existsById((long)2)) {
            utenteRepository.save(new Utente("Francesco", "Gatto", "francescogatto2001@gmail.com", "11/01/2001","M","7179258205463811", Utente.DEFAULT_TOKEN, false));
        }

        if (!utenteRepository.existsById((long)3)) {
            utenteRepository.save(new Utente("Giorgio", "Castelluccio", "giorgio_castelluccio@outlook.it", "10/10/2010","M","7168367147841000", Utente.DEFAULT_TOKEN, false));
        }

        //Aggiunta ruolo
        if (!ruoloRepository.existsRuoloByNome("Partecipante")) {
            ruoloRepository.save(new Ruolo("Partecipante"));
        }

        if (!ruoloRepository.existsRuoloByNome("Organizzatore")) {
            ruoloRepository.save(new Ruolo("Organizzatore"));
        }

        if (!ruoloRepository.existsRuoloByNome("Organizzatore_Master")) {
            ruoloRepository.save(new Ruolo("Organizzatore_Master"));
        }

        //Aggiunta della Categoria
        if(!categoriaRepository.existsById((long)1)){
            categoriaRepository.save(new Categoria("Lavoro", "Categoria per il lavoro"));
        }

        if(!categoriaRepository.existsById((long)2)){
            categoriaRepository.save(new Categoria("Scuola", "Categoria per la scuola"));
        }

        if(!categoriaRepository.existsById((long)3)){
            categoriaRepository.save(new Categoria("Divertimento", "Categoria per il divertimento"));
        }

        //Aggiunta Immagine
        Immagine lavoro1 = new Immagine("https://www.lavoro1.com/path/to/lavoro1.txt");
        if(!immagineRepository.existsById((long)1)){
            immagineRepository.save(lavoro1);
        }

        Immagine scuola1 = new Immagine("https://www.scuola1.com/path/to/scuola1.txt");
        if(!immagineRepository.existsById((long)2)){
            immagineRepository.save(scuola1);
        }

        Immagine divertimento1 = new Immagine("https://www.divertimento1.com/path/to/divertimento1.txt");
        if(!immagineRepository.existsById((long)3)){
           immagineRepository.save(divertimento1);
        }

        //Aggiunta dello Scenario

        if(!scenarioRepository.existsById((long)1)){
            scenarioRepository.save(new Scenario("Lavoro1", "Scenario 1 per il lavoro", immagineRepository.findImmagineById((long)1), (long)1));
        }

        if(!scenarioRepository.existsById((long)2)){
            scenarioRepository.save(new Scenario("Scuola1", "Scenario 1 per la scuola", immagineRepository.findImmagineById((long)2), (long)2));
        }

        if(!scenarioRepository.existsById((long)3)){
            scenarioRepository.save(new Scenario("Divertimento1", "Divertimento 1 per la scuola", immagineRepository.findImmagineById((long)3), (long)3));
        }

        //Aggiunta della Stanza
        if(!stanzaRepository.existsById((long)1)){
            stanzaRepository.save(new Stanza("StanzaLavoro1", "Stanza 1 per il lavoro", true, 500, scenarioRepository.findScenarioById((long)1), "000001"));
        }

        if(!stanzaRepository.existsById((long)2)){
            stanzaRepository.save(new Stanza("StanzaScuola1", "Stanza 1 per la scuola", false, 200, scenarioRepository.findScenarioById((long)2), "000002"));
        }

        if(!stanzaRepository.existsById((long)3)){
           stanzaRepository.save(new Stanza("StanzaDivertimento1", "Stanza 1 per il divertimento", true, 50, scenarioRepository.findScenarioById((long)3), "000003"));
        }

        //Aggiunta dello StatoPartecipazione
        //Stanza lavoro
        Stanza lavoro = stanzaRepository.findStanzaById((long)1);
        Utente michele = utenteRepository.findFirstByMetaId("7184488154978627");
        Utente francesco = utenteRepository.findFirstByMetaId("7179258205463811");
        Utente giorgio = utenteRepository.findFirstByMetaId("7168367147841000");

        if(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(michele, lavoro) == null){
            statoPartecipazioneRepository.save(new StatoPartecipazione(lavoro, michele, stanzaService.getRuolo(ORGANIZZATORE_MASTER), false, false, "Michele"));
        }

        if(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(francesco, lavoro) == null){
            statoPartecipazioneRepository.save(new StatoPartecipazione(lavoro, francesco, stanzaService.getRuolo(Ruolo.ORGANIZZATORE), false, false, "Francesco"));
        }

        if(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(giorgio, lavoro) == null) {
            statoPartecipazioneRepository.save(new StatoPartecipazione(lavoro, giorgio, stanzaService.getRuolo(Ruolo.PARTECIPANTE), true, false, "Giorgio"));
        }

        //Stanza scuola
        Stanza scuola = stanzaRepository.findStanzaById((long)2);

        if(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(michele, scuola) == null){
            statoPartecipazioneRepository.save(new StatoPartecipazione(scuola, michele, stanzaService.getRuolo(Ruolo.PARTECIPANTE), false, true, "Michele"));
        }

        if(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(francesco, scuola) == null){
            statoPartecipazioneRepository.save(new StatoPartecipazione(scuola, francesco, stanzaService.getRuolo(Ruolo.PARTECIPANTE), false, false, "Francesco"));
        }

        if(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(giorgio, scuola) == null) {
            statoPartecipazioneRepository.save(new StatoPartecipazione(scuola, giorgio, stanzaService.getRuolo(ORGANIZZATORE_MASTER), false, false, "Giorgio"));
        }

        //Stanza divertimento
        Stanza divertimento = stanzaRepository.findStanzaById((long)3);

        if(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(michele, divertimento) == null){
            statoPartecipazioneRepository.save(new StatoPartecipazione(divertimento, michele, stanzaService.getRuolo(Ruolo.PARTECIPANTE), true, false, "Michele"));
        }

        if(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(francesco, divertimento) == null){
            statoPartecipazioneRepository.save(new StatoPartecipazione(divertimento, francesco, stanzaService.getRuolo(Ruolo.PARTECIPANTE), false, false, "Francesco"));
        }

        if(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(giorgio, divertimento) == null) {
            statoPartecipazioneRepository.save(new StatoPartecipazione(divertimento, giorgio, stanzaService.getRuolo(ORGANIZZATORE_MASTER), false, false, "Giorgio"));
        }
    }

}
