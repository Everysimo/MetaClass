package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.MetaClassApplicationTests;
import com.commigo.metaclass.MetaClass.entity.Ruolo;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.MismatchJsonProperty;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @InjectMocks
    private GestioneStanzaServiceImpl gestioneStanzaServiceImpl;

    private Long codiceStanza;
    private String idUtente;
    private Stanza stanza;
    private Utente utente;
    private Ruolo ruolo;
    private StatoPartecipazione statoPartecipazione;

    /**
     *  Questo metodo viene richiamato prima di ogni test e qui vanno inizializzate
     *  tutte le variabili che andrete ad utilizzare
     */
    @BeforeEach
    public void setUp() throws IOException, MismatchJsonProperty {
        codiceStanza = 123123L;
        idUtente = new String("1");
        stanza = new Stanza("Nome","Descrizione",true,25,1L);
        utente = new Utente();
        utente.setId(Long.parseLong(idUtente));
        statoPartecipazione = new StatoPartecipazione(stanza,utente, new Ruolo(Ruolo.ORGANIZZATORE),false,false,
                "Giorgio", false);
        ruolo = new Ruolo(Ruolo.PARTECIPANTE);
    }

    /*
    @Test
    void banOrganizzatoreOnSuccess() throws RuntimeException403, ServerRuntimeException {
        when(utenteRepository.findUtenteById(utente.getId())).thenReturn(utente);
        when(statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(utente, stanza)).
                thenReturn(statoPartecipazione);

        when(ruoloRepository.findByNome("Partecipante")).thenReturn(ruolo);
        when(statoPartecipazioneRepository.save(statoPartecipazione)).thenReturn(null);
        Response response = gestioneStanzaServiceImpl.banOrganizzatore(stanza, "aaa", Long.parseLong(idUtente)).getBody();

        assertEquals("Organizzatore bannato con successo", response.getMessage());

    }*/


}
