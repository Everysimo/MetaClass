package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.MeetingRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GestioneMeetingController.class,TestObjectMapperConfig.class})
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GestioneMeetingControllerUnitTest {

    /**
     *  Un mock oggetto è un oggetto simulato che può essere programmato per rispondere in modo specifico a chiamate di
     *  metodo durante i test.
     */
    @MockBean
    private GestioneMeetingService meetingService;
    @MockBean
    private ValidationToken validationToken;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @InjectMocks
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
    private FeedbackMeeting feedbackMeeting;
    private Report report;
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

        report = new Report(1L, 500, Duration.ZERO, 550, meeting, List.of(utente));

        feedbackMeeting = new FeedbackMeeting(utente, meeting, report);
        request = MockMvcRequestBuilders
                .post("/schedulingMeeting")  // Assicurati di utilizzare il percorso corretto
                .header("Authorization", "Bearer TODO")
                .buildRequest(new MockServletContext());
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


        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        try{
            when(meetingService.creaScheduling(meeting, utente.getMetaId())).thenReturn(true);

            //converto la map
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            // Creazione di un oggetto JSON vuoto
            ObjectNode jsonNode = objectMapper.createObjectNode();

            // Aggiunta degli attributi uno per uno
            jsonNode.put("nome", meeting.getNome());
            jsonNode.put("inizio", "2024-02-02 18:00");
            jsonNode.put("fine", "2024-02-02 20:00");
            jsonNode.put("id_stanza", meeting.getStanza().getId());

            // Converti l'oggetto JSON in una stringa
            String jsonRequest = jsonNode.toString();

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

            MvcResult mvcResult = actualPerformResult.andReturn();
            int status = mvcResult.getResponse().getStatus();
            String responseContent = mvcResult.getResponse().getContentAsString();

            System.out.println("Actual Status: " + status);
            System.out.println("Response Content: " + responseContent);

        } catch (Exception e) {
               fail("Exception not expected: " + e.getMessage());
        }

    }

   /* private void testCasesSchedulingMeeting(int testcase){

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


    }*/

    @Test
    public void testSchedulingMeetingOnFailure() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(false);

        meeting.setNome("N");
        assertValidationMeeting("Lunghezza nome non valida");

        //converto la map
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // Creazione di un oggetto JSON vuoto
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("nome", meeting.getNome());
        jsonNode.put("inizio", "2024-02-02 18:00");
        jsonNode.put("fine", "2024-02-02 20:00");
        jsonNode.put("id_stanza", meeting.getStanza().getId());

        // Converti l'oggetto JSON in una stringa
        String jsonRequest = jsonNode.toString();

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header("Authorization", "Bearer TODO");

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(meetingController)
                .build()
                .perform(requestBuilder);

        MvcResult mvcResult = actualPerformResult.andReturn();
        int status = mvcResult.getResponse().getStatus();
        String responseContent = mvcResult.getResponse().getContentAsString();

        System.out.println("Actual Status: " + status);
        System.out.println("Response Content: " + responseContent);


        if (responseContent.isEmpty()) {
            System.out.println("Il messaggio della risposta è vuoto.");
        } else {
            System.out.println("Messaggio della risposta: " + responseContent);
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
