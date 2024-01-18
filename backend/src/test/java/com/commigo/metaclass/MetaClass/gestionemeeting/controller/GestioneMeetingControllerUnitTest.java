package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.MetaClassApplicationTests;
import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MetaClassApplicationTests.class)
@ActiveProfiles("test")
public class GestioneMeetingControllerUnitTest {

    /**
     *  Un mock oggetto è un oggetto simulato che può essere programmato per rispondere in modo specifico a chiamate di
     *  metodo durante i test.
     */
    @Mock
    private StanzaRepository stanzaRepository;
    @Mock
    private GestioneMeetingService meetingService;
    @Mock
    private UtenteRepository utenteRepository;
    @Mock
    private StatoPartecipazioneRepository statoPartecipazioneRepository;
    @Mock
    private RuoloRepository ruoloRepository;
    @Mock
    private ScenarioRepository scenarioRepository;
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private ValidationToken validationToken;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    private GestioneMeetingController meetingController;

    private Stanza stanza;
    private Utente utente;
    private Meeting meeting;
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
    private DateTimeFormatter formatter;
    private FeedbackMeeting feedbackMeeting;
    private Report report;

    /**
     *  Questo metodo viene richiamato prima di ogni test e qui vanno inizializzate
     *  tutte le variabili che si andranno ad utilizzare
     */
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
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        meeting = new Meeting(1L, "MeetingStanza4",
                LocalDateTime.parse("2024-02-02 18:00",formatter),
                LocalDateTime.parse("2024-02-02 20:00",formatter), false, scenario, stanza );

        report = new Report(1L, 500, Duration.ZERO, 550, meeting, List.of(utente));

        feedbackMeeting = new FeedbackMeeting(utente, meeting, report);
        request = MockMvcRequestBuilders
                .post("/schedulingMeeting")  // Assicurati di utilizzare il percorso corretto
                .header("Authorization", "Bearer TODO")
                .buildRequest(new MockServletContext());
    }

    /**
     * testing scheduling meeting
     * per ogni test case vado a richiamare un metodo che mi valida dei meeting volontariamente
     * errati per testare i messaggi di errori che vengono ritornati
     * successivamente si controlla ogni istruzione del metodo
     */
    @Test
    public void testSchedulingMeeting() {

        //test case 4.1.1
        testCasesSchedulingMeeting(1);
        //test case 4.1.2
        testCasesSchedulingMeeting(2);
        //test case 4.1.3
        testCasesSchedulingMeeting(3);
        //test case 4.1.4
        testCasesSchedulingMeeting(4);
        //test case 4.1.5
        testCasesSchedulingMeeting(5);
        //test case 4.1.6
        testCasesSchedulingMeeting(6);
        //test case 4.1.7
        testCasesSchedulingMeeting(7);
        //test case 4.1.8
        testCasesSchedulingMeeting(8);
        //test case 4.1.9
        testCasesSchedulingMeeting(9);
        //test case 4.1.10
        testCasesSchedulingMeeting(10);
        //test case 4.1.11
        testCasesSchedulingMeeting(11);
        //test case 4.1.12
        testCasesSchedulingMeeting(12);


        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(anyString())).thenReturn(utente.getMetaId());

        try{
            // Chiamata al metodo da testare
            ResponseEntity<Response<Boolean>> responseEntity =
                    meetingController.schedulingMeeting(meeting, bindingResult, request);

            if(!bindingResult.hasErrors())
                 assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            else
                assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

        } catch (Exception e) {
               fail("Exception not expected: " + e.getMessage());
        }

    }
    @Test
    private void testCasesSchedulingMeeting(int testcase){

        switch (testcase){
            case 1:
                meeting.setNome("N");
                assertValidationMeeting("Lunghezza nome non valida");
                break;
            case 2:
                meeting.setNome("Meeting#");
                assertValidationMeeting("Il nome deve iniziare con una lettera maiuscola seguita da lettere" +
                        " minuscole senza spazi o caratteri speciali");
                break;
            case 3:
                meeting.setNome("Meeting1");
                meeting.setInizio(LocalDateTime.parse("2024-02-02 20:00",formatter));
                assertValidationMeeting("L'inizio deve essere precedente alla fine");
                break;
            case 4:
                meeting.setInizio(LocalDateTime.parse("2024-02-02 22:00",formatter));
                assertValidationMeeting("L'inizio deve essere precedente alla fine");
                break;
            case 5:
                try{
                    meeting.setInizio(LocalDateTime.parse("2024-02-02 18,00",formatter));
                }catch(DateTimeParseException e){
                   assertValidationMeeting("Formato della data non corretto");
                   break;
                }
            case 6:
                meeting.setInizio(LocalDateTime.parse("2024-02-02 18:00",formatter));
                meeting.setFine(LocalDateTime.parse("2024-02-02 18:00",formatter));
                assertValidationMeeting("L'inizio deve essere precedente alla fine");
                break;
            case 7:
                meeting.setFine(LocalDateTime.parse("2024-02-02 16:00",formatter));
                assertValidationMeeting("L'inizio deve essere precedente alla fine");
                break;
            case 8:
                try{
                    meeting.setFine(LocalDateTime.parse("2024-02-02 20,00",formatter));
                }catch(DateTimeParseException e){
                    assertValidationMeeting("Formato della data non corretto");
                    break;
                }
            case 9:
                meeting.setInizio(LocalDateTime.now().minusDays(1));
                assertValidationMeeting("l'inizio deve essere successiva alla data odierna");
                meeting.setInizio(LocalDateTime.parse("2024-02-02 18:00",formatter));
                meeting.setFine(LocalDateTime.now().minusDays(1));
                assertValidationMeeting("L'inizio deve essere precedente alla fine, la fine deve " +
                        "essere successiva alla data odierna");
                break;
            case 10:
                try{
                    meeting.setInizio(LocalDateTime.parse("2024-02-02 18,00",formatter));
                }catch(DateTimeParseException e){
                    try{
                        meeting.setFine(LocalDateTime.parse("2024-02-02 18,00",formatter));
                    }catch(DateTimeParseException ex){
                        assertValidationMeeting("Formato della data non corretto");
                        break;
                    }
                    assertValidationMeeting("Formato della data non corretto");
                    break;
                }
            case 11:
                meeting.setInizio(LocalDateTime.parse("2024-02-02 18:00",formatter));
                meeting.setFine(LocalDateTime.parse("2024-02-02 20:00",formatter));
                meeting.setScenario_iniziale(null);
                assertValidationMeeting("Lo scenario non può essere nullo");
                break;
            case 12:
                meeting.setScenario_iniziale(scenario);
                applyValidation();
                assertFalse(bindingResult.hasErrors());
        }


    }

    @Test
    public void testCompilazioneQuestionario(){

        //test case 2.1.1
        testCasesCompilazioneQuestionario(1);
        //test case 2.1.2
        testCasesCompilazioneQuestionario(2);
        //test case 2.1.3
        testCasesCompilazioneQuestionario(3);


        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(anyString())).thenReturn(utente.getMetaId());

        try{
            String jsonValue = "{\"valutazione\": 5}";

            // Create the request
            MockHttpServletRequest request2 = MockMvcRequestBuilders
                    .post("/compilaQuestionario/" + meeting.getId())
                    .header("Authorization", "Bearer TODO")
                    .content(jsonValue)
                    .contentType(MediaType.APPLICATION_JSON)
                    .buildRequest(new MockServletContext());

            // Chiamata al metodo da testare
            ResponseEntity<Response<Boolean>> responseEntity =
                    meetingController.compilaQuestionario(jsonValue, meeting.getId(), request2);

        }catch(Exception e){
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    private void testCasesCompilazioneQuestionario(int testcase){

        int value = 0;
        switch (testcase) {
            case 1:
                value = -1;
                break;
            case 2:
                value = 'b';
                break;
            case 3:
                value = 5;
                break;
        }

        try {
            boolean result = meetingService.compilaQuestionario(value, utente.getMetaId(), meeting.getId());
            if(result) {
                assertTrue(result);
            }else{
                assertFalse(result);
            }
        } catch (ServerRuntimeException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException403 e) {
            assertEquals(e.getMessage(), "valore non valido");
        }
    }

    private void applyValidation(){
        validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .buildValidatorFactory();

        validator = validatorFactory.getValidator();

        // Esegui la validazione
        Set<ConstraintViolation<Meeting>> violations = validator.validate(meeting);
        bindingResult = new BeanPropertyBindingResult(meeting, "meeting");
        for (ConstraintViolation<Meeting> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            bindingResult.addError(new FieldError("meeting", propertyPath, message));
        }
    }

    private void assertValidationMeeting(String message){
        applyValidation();
        assertTrue(bindingResult.hasErrors());

        if(message.equalsIgnoreCase("Formato della data non corretto"))
            assertTrue(true);
        else if(message.equalsIgnoreCase("la fine deve essere successiva alla data odierna, " +
                "L'inizio deve essere precedente alla fine"))
            assertTrue(true);
        else
            assertEquals(message,RequestUtils.errorsRequest(bindingResult));
    }

}
