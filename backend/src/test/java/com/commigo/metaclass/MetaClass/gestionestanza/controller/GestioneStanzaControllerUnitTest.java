package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import com.commigo.metaclass.MetaClass.MetaClassApplicationTests;
import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.controller.GestioneMeetingController;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.MeetingRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MetaClassApplicationTests.class)
@ActiveProfiles("test")
public class GestioneStanzaControllerUnitTest {

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
    @Mock
    private ValidationToken validationToken;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    private GestioneStanzaControl stanzaController;

    private Stanza stanza;
    private Utente utente;
    private Ruolo partecipante;
    private Ruolo organizzatore;
    private Ruolo organizzatore_master;
    private StatoPartecipazione statoPartecipazione;
    private Immagine immagine;
    private Categoria categoria;
    private Scenario scenario;
    private HttpServletRequest request;
    private ValidatorFactory validatorFactory;
    private Validator validator;
    private BindingResult bindingResult;


    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
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
                organizzatore_master, false, false,
                "Michele", false);

        request = MockMvcRequestBuilders
                .post("/creastanza")  // Assicurati di utilizzare il percorso corretto
                .header("Authorization", "Bearer TODO")
                .buildRequest(new MockServletContext());
    }

    @Test
    public void testCreaStanza() {

        //test case 3.1.1
        testCasesCreaStanza(1);
        //test case 3.1.2
        testCasesCreaStanza(2);
        //test case 3.1.3
        testCasesCreaStanza(3);
        //test case 3.1.4
        testCasesCreaStanza(4);
        //test case 3.1.5
        testCasesCreaStanza(5);
        //test case 3.1.6
        testCasesCreaStanza(6);
        //test case 3.1.7
        testCasesCreaStanza(7);
        //test case 3.1.8
        testCasesCreaStanza(8);
        //test case 3.1.9
        testCasesCreaStanza(9);
        //test case 3.1.10
        testCasesCreaStanza(10);
        //test case 3.1.11
        testCasesCreaStanza(11);
        //test case 3.1.12
        testCasesCreaStanza(12);
        //test case 3.1.13
        testCasesCreaStanza(13);

        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(anyString())).thenReturn(utente.getMetaId());

        try{
            // Chiamata al metodo da testare
            ResponseEntity<Response<Boolean>> responseEntity =
                    stanzaController.creaStanza(stanza, bindingResult, request);

            if(!bindingResult.hasErrors())
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            else
                assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }
    @Test
    private void testCasesCreaStanza(int testcase){

        switch (testcase){
            case 1:
                stanza.setNome("C");
                assertValidationStanza("Lunghezza nome non valida");
                break;
            case 2:
                stanza.setNome("1Stanza");
                assertValidationStanza("Formato nome errato");
                break;
            case 3:
                stanza.setCodice("1");
                assertValidationStanza("Lunghezza codice_stanza errato");
                break;
            case 4:
                stanza.setCodice("12345H");
                assertValidationStanza("Formato codice_stanza errato");
                break;
            case 5:
                stanza.setDescrizione("");
                assertValidationStanza("Lunghezza descrizione errata");
                break;
            case 6:
                stanza.setDescrizione("La stanza del meeting è illuminata da una soffusa luce, le pareti bianche e arredate con eleganti dipinti creano un'atmosfera raffinata. Al centro, un grande tavolo ovale circondato da comode sedie offre uno spazio accogliente per discussioni produttive. La tecnologia avanzata integra schermi discreti, facilitando la condivisione di idee in un ambiente moderno e confortevole");
                assertValidationStanza("Lunghezza descrizione errata");
                break;
            case 7:
                stanza.setDescrizione("questa descrizione funge da esempio");
                assertValidationStanza("Formato descrizione errata");
            case 8:
                stanza.setTipo_Accesso(Boolean.parseBoolean(null));;
                assertValidationStanza("Il tipo di accesso non può essere nullo");
                break;
            case 9:
                stanza.setMax_Posti(0);
                assertValidationStanza("Il valore del  parametro non deve essere inferiore ad 1");
                break;
            case 10:
                stanza.setMax_Posti(1000);
                assertValidationStanza("Il valore del  parametro non deve superare 999");
                break;
            case 11:
                stanza.setMax_Posti('A');
                assertValidationStanza("Il valore del  parametro non deve superare 999");
                break;
            case 12:
                stanza.setScenario(null);
                assertValidationStanza("Lo scenario non può essere nullo");
                break;
            case 13:
                stanza.setScenario(scenario);
                applyValidation();
                assertFalse(bindingResult.hasErrors());
        }


    }

    private void applyValidation(){
        validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .buildValidatorFactory();

        validator = validatorFactory.getValidator();

        // Esegui la validazione
        Set<ConstraintViolation<Stanza>> violations = validator.validate(stanza);
        bindingResult = new BeanPropertyBindingResult(stanza, "stanza");
        for (ConstraintViolation<Stanza> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            bindingResult.addError(new FieldError("stanza", propertyPath, message));
        }
    }

    private void assertValidationStanza(String message){
        applyValidation();
        assertTrue(bindingResult.hasErrors());

        if(message.equalsIgnoreCase("Formato della data non corretto"))
            assertTrue(true);
        else if(message.equalsIgnoreCase("la fine deve essere successiva alla data odierna, " +
                "L'inizio deve essere precedente alla fine"))
            assertTrue(true);
        else
            assertEquals(message, RequestUtils.errorsRequest(bindingResult));
    }

}
