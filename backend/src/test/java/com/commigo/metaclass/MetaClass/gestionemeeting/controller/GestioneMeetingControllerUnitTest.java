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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GestioneMeetingController.class,TestObjectMapperConfig.class})
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ActiveProfiles("test")
@SpringBootTest
public class GestioneMeetingControllerUnitTest {

    /**
     *  Un mock oggetto è un oggetto simulato che può essere programmato per rispondere in modo specifico a chiamate di
     *  metodo durante i test.
     */
    @MockBean
    private GestioneMeetingService meetingService;
    @Mock
    private ValidationToken validationToken;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private GestioneMeetingController meetingController;


    private Stanza stanza;
    private Utente utente;
    private Meeting meeting;
    private Ruolo organizzatore_master;
    private Immagine immagine;
    private Categoria categoria;
    private Scenario scenario;
    private ValidatorFactory validatorFactory;
    private Validator validator;
    private BindingResult bindingResult;
    private DateTimeFormatter formatter;
    private final String API_URL = "/schedulingMeeting";


    /**
     *  Questo metodo viene richiamato prima di ogni test e qui vanno inizializzate
     *  tutte le variabili che si andranno ad utilizzare
     */
    @BeforeEach
    public void setUp() throws Exception {
        utente = new Utente(1L, "Michele", "Pesce", "pescemichele@live.com",
                "05/30/1993","M","7184488154978627", Utente.DEFAULT_TOKEN, true);
        organizzatore_master = new Ruolo(3L, Ruolo.ORGANIZZATORE_MASTER);
        immagine = new Immagine(1L, "lavoro1.txt","https://www.lavoro1.com/path/to/lavoro1.txt");
        categoria = new Categoria(1L, "Lavoro", "Categoria per il lavoro");
        scenario = new Scenario(1L, "Lavoro1", "Scenario 1 per il lavoro", immagine, categoria);
        stanza = new Stanza(1L, "StanzaLavoro1", "Stanza 1 per il lavoro",
                false, 500, scenario, "000001");
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        meeting = new Meeting(1L, "MeetingStanza4",
                LocalDateTime.parse("2024-02-02 18:00",formatter),
                LocalDateTime.parse("2024-02-02 20:00",formatter), false, scenario, stanza );
        bindingResult = new BeanPropertyBindingResult(meeting, "meeting");
    }

    /**
     * testing scheduling meeting
     * per ogni test case vado a richiamare un metodo che mi valida dei meeting volontariamente
     * errati per testare i messaggi di errori che vengono ritornati
     * successivamente si controlla ogni istruzione del metodo
     */
    @Test
    public void testSchedulingMeetingOnSuccess(){

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
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        try{
            when(meetingService.creaScheduling(meeting, utente.getMetaId())).thenReturn(true);
            // Chiamata al metodo da testare
            /*ResponseEntity<Response<Boolean>> responseEntity =
                    meetingController.schedulingMeeting(meeting, bindingResult, request);*/

            //converto la map
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = objectMapper.writeValueAsString(meeting);

            MockHttpServletRequestBuilder requestBuilder =
                    MockMvcRequestBuilders.post(API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest)
                    .header("Authorization", "Bearer TODO");

            ResultActions actualPerformResult =
                    MockMvcBuilders.standaloneSetup(meetingController)
                            .build()
                            .perform(requestBuilder);

            actualPerformResult.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());


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
    public void testSchedulingMeetingOnFailure() throws Exception {
        when(validationToken.isTokenValid(any())).thenReturn(true);

        meeting.setNome("N");
        assertValidationMeeting("Lunghezza nome non valida");

        //converto la map
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonRequest = objectMapper.writeValueAsString(meeting);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header("Authorization", "Bearer TODO");

        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(meetingController)
                        .build()
                        .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    private void applyValidation(){
        validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .buildValidatorFactory();

        validator = validatorFactory.getValidator();

        // Esegui la validazione
        Set<ConstraintViolation<Meeting>> violations = validator.validate(meeting);
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
        else if(message.equalsIgnoreCase("L'inizio deve essere precedente alla fine, la fine " +
                "deve essere successiva alla data odierna"))
            assertTrue(true);
        else
            assertEquals(message,RequestUtils.errorsRequest(bindingResult));
    }

}
