package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.MetaClassApplicationTests;
import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.MismatchJsonProperty;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.inizializer.DataInitializer;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MetaClassApplicationTests.class)
@ActiveProfiles("test")
public class GestioneStanzaServiceUnitTest {

    /**
     *  Un mock oggetto è un oggetto simulato che può essere programmato per rispondere in modo specifico a chiamate di
     *  metodo durante i test.
     */
    @Mock
    private StanzaRepository stanzaRepository;
    @Mock
    private UtenteRepository utenteRepository;
    @Mock
    private StatoPartecipazioneRepository statoPartecipazioneRepository;
    @Mock
    private RuoloRepository ruoloRepository;
    @Mock
    private ScenarioRepository scenarioRepository;
    @InjectMocks
    private GestioneStanzaServiceImpl gestioneStanzaServiceImpl;
    private Stanza stanza;
    private Utente utente;
    private Ruolo partecipante;
    private Ruolo organizzatore;
    private Ruolo organizzatore_master;
    private StatoPartecipazione statoPartecipazione;
    private Immagine immagine;
    private Categoria categoria;
    private Scenario scenario;

    /**
     *  Questo metodo viene richiamato prima di ogni test e qui vanno inizializzate
     *  tutte le variabili che andrete ad utilizzare
     */
    @BeforeEach
    public void setUp() throws Exception {
        utente = new Utente(1L, "Michele", "Pesce", "pescemichele@live.com",
                "05/30/1993","M","7184488154978627", Utente.DEFAULT_TOKEN, true);
        partecipante = new Ruolo(1L, Ruolo.PARTECIPANTE);
        organizzatore = new Ruolo(2L, Ruolo.ORGANIZZATORE);
        organizzatore_master = new Ruolo(3L, Ruolo.ORGANIZZATORE_MASTER);
        immagine = new Immagine(1L, "lavoro1.txt","https://www.lavoro1.com/path/to/lavoro1.txt");
        categoria = new Categoria(1L, "Lavoro", "Categoria per il lavoro");
        scenario = new Scenario(1L, "Lavoro1", "Scenario 1 per il lavoro", immagine, categoria);
        stanza = new Stanza(1L, "StanzaLavoro1", "Stanza 1 per il lavoro",
                false, 500, scenario, "000001");
        statoPartecipazione = new StatoPartecipazione(stanza, utente,
                new Ruolo(1L, "Organizzatore_Master"), false, false,
                "Michele", false);
    }

    /*
    MICHELE:
             per effettuare i test di unità dovete prendere ogni signola istruzione
             del metodo che state testando e dovete usare la forma:
             when(istruzione).thenReturn(valore di ritorno del metodo)

             se nell'istruzione sono richiesti parametri perchè magari
             si stanno chiamando altri metodi potete usare any() al posto di parametri reali

             infine dovete testare il metodo nella sua interezza, nel senso che bisogna
             invocare il metodo e se ritorna un boolean usare assertTrue(result)
             se invece è un oggetto ResponseEntity usate:
                   assertEquals(messaggio valido, messaggio ritornato)
             oppure
                   assertEquals(value valido, value ritornato)

             quando si richiama la funzione sono necessarie le classi concrete e non
             si utilizzano i metodi any(). alcune classi sono state aggiunte.
             ovviamente se sono richieste nella funzionalità specifica le aggiungete.
             Consiglio: usate quelle che stanno nel DataInizializer
     */

   /* @Test
    void banOrganizzatoreOnSuccess() throws RuntimeException403, ServerRuntimeException {
        when(utenteRepository.findUtenteById(utente.getId())).thenReturn(utente);
        when(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(utente, stanza)).
                thenReturn(statoPartecipazione);

        when(ruoloRepository.findByNome("Partecipante")).thenReturn(partecipante);
        when(statoPartecipazioneRepository.save(statoPartecipazione)).thenReturn(null);
        Response response = gestioneStanzaServiceImpl.banOrganizzatore(stanza.getId(), "aaa", Long.parseLong(idUtente)).getBody();

        assertEquals("Organizzatore bannato con successo", response.getMessage());

    }*/

    @Test
    public void creaStanzaOnSuccess() throws Exception {

        //verifica della funzione: ritorno -> Utente
        when(utenteRepository.findFirstByMetaId(anyString())).thenReturn(utente);

        //verifica ricerca di una scenario nel database
        when(scenarioRepository.findScenarioById(anyLong())).thenReturn(scenario);

        //salvataggio nel database della stanza
        when(stanzaRepository.save(any(Stanza.class))).thenReturn(stanza);

        //ricerca dell'ultima tupla dell'entità stanza
        when(stanzaRepository.findIdUltimaTupla()).thenReturn(1L);

        //TEST ACCESSO ALLA STANZA
        //salvataggio dello stato partecipazione
        when(statoPartecipazioneRepository.save(any(StatoPartecipazione.class)))
                .thenReturn(statoPartecipazione);

        // Chiamata al metodo
        boolean result = gestioneStanzaServiceImpl.creaStanza(stanza,utente.getMetaId());

        // Verifica il risultato atteso
        assertTrue(result);
    }


}
