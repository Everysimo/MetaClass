package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.DataFormatException;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.MeetingRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
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
import org.mockito.Mockito;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = GestioneMeetingController.class)
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
    @MockBean
    private StanzaRepository stanzaRepository;
    @InjectMocks
    private GestioneMeetingController meetingController;

    private static final int CLIENT_ERROR_STATUS = 400;
    private static final int SUCCESSFUL_STATUS = 200;
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
    private DateTimeFormatter notCorrectFormatter;
    private FeedbackMeeting feedbackMeeting;
    private Report report;
    private final String API_URL_Scheduling = "/schedulingMeeting";
    private final String API_URL_Compilazione = "/compilaQuestionario/1";
    private Integer valutazione;


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
        notCorrectFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH,mm");
        meeting = new Meeting(1L, "MeetingStanza4",
                LocalDateTime.parse("2024-02-02 18:00",formatter),
                LocalDateTime.parse("2024-02-02 20:00",formatter), false, scenario, stanza );

        report = new Report(1L, 500, Duration.ZERO, 550, meeting, List.of(utente));

        feedbackMeeting = new FeedbackMeeting(utente, meeting, report);
        bindingResult = new BeanPropertyBindingResult(meeting, "meeting");
    }

    private String JSONConvertitorMeeting(Meeting meeting){
        //CREAAZIONE DEL BODY DELLA RICHIESTA
        //Converto l'istanza meeting in una stringa JSON accettabile del controller
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("nome", meeting.getNome());
        jsonNode.put("id_stanza", meeting.getStanza().getId());
        jsonNode.put("inizio", meeting.getInizio().format(formatter));
        jsonNode.put("fine", meeting.getFine().format(formatter));

        return jsonNode.toString();
    }

    /**
     * metodo che prende un meeting e invia la richiesta al server
     * @param meeting
     * @retrun ResultActions
     * @throws Exception
     */
    private ResultActions sendRequestMeeting(Meeting meeting) throws Exception {


        //formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL_Scheduling)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONConvertitorMeeting(meeting))     //metodo privato della classe
                        .header("Authorization", "Bearer TODO");

        //ritorno della risposta
        return  MockMvcBuilders.standaloneSetup(meetingController)
                        .build()
                        .perform(requestBuilder);
    }

    public ResultActions sendBadRequestMeeting(Meeting meeting, String inizio, String fine) throws Exception {

        //CREAZIONE DEL BODY DELLA RICHIESTA
        //Converto l'istanza meeting in una stringa JSON accettabile del controller
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("nome", meeting.getNome());
        jsonNode.put("inizio", "2024-02-02 18,00");
        jsonNode.put("fine", fine);
        jsonNode.put("id_stanza", 1);
        System.out.println(meeting.getInizio().format(formatter));

        //formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL_Scheduling)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNode.toString())     //metodo privato della classe
                        .header("Authorization", "Bearer TODO");

        //ritorno della risposta
        return  MockMvcBuilders.standaloneSetup(meetingController)
                .build()
                .perform(requestBuilder);

    }

    //metodo che produce una richiesta testa un errore 500 durante la richiesta
    private void sendRequestServerFailureMeetinng(Meeting meeting) throws Exception {
        // Formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_URL_Scheduling)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONConvertitorMeeting(meeting))  // Metodo privato della classe
                .header("Authorization", "Bearer TODO");

        try {
            // Forza il metodo creaScheduling a lanciare un'eccezione ServerRuntimeException
            doThrow(ServerRuntimeException.class)
                    .when(meetingService).creaScheduling(any(), any());

            // Esecuzione della richiesta e ritorno della risposta
            MvcResult result = MockMvcBuilders
                    .standaloneSetup(meetingController)
                    .build()
                    .perform(requestBuilder)
                    .andReturn();

            // Verifica del codice di stato
            int statusCode = result.getResponse().getStatus();
            assertThat(statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        } finally {
            // Ripristina il comportamento normale del metodo creaScheduling dopo il test
            Mockito.reset(meetingService);
        }
    }


    private void testExpectedResult(int status, ResultActions actualPerformResult) throws Exception {

        if(status==CLIENT_ERROR_STATUS)
            actualPerformResult.andExpect(status().is4xxClientError());
        else if(status==SUCCESSFUL_STATUS)
            actualPerformResult.andExpect(status().is2xxSuccessful());

        MvcResult mvcResult = actualPerformResult.andReturn();
        int resultStatus = mvcResult.getResponse().getStatus();
        String responseContent = mvcResult.getResponse().getContentAsString();

        System.out.println("Actual Status: " + resultStatus);
        System.out.println("Response Content: " + responseContent);
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

            //vedere i metodi private testExpectedResult e sendRequest
            testExpectedResult(SUCCESSFUL_STATUS, sendRequestMeeting(meeting));

        } catch (Exception e) {
               fail("Exception not expected: " + e.getMessage());
        }

    }

    @Test
    public void testSchedulingMeetingOnTokenValidationFailed() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(false);

        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));

    }

    @Test
    public void testSchedulingMeetingOnServerFailure() throws Exception {
        when(validationToken.isTokenValid(any())).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(meetingService.creaScheduling(meeting, utente.getMetaId()))
                .thenThrow(ServerRuntimeException.class);

        //vedere i metodi private testExpectedResult e sendRequest
        sendRequestServerFailureMeetinng(meeting);
    }

    @Test
    public void testSchedulingMeetingOnTestCase1() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        meeting.setNome("N");
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));

    }

    @Test
    public void testSchedulingMeetingOnTestCase2() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        meeting.setNome("Meeting#");
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));

    }

    @Test
    public void testSchedulingMeetingOnTestCase3() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        meeting.setNome("Meeting1");
        meeting.setInizio(LocalDateTime.parse("2024-02-02 20:00",formatter));
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));

    }

    @Test
    public void testSchedulingMeetingOnTestCase4() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        meeting.setInizio(LocalDateTime.parse("2024-02-02 22:00",formatter));
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));

    }

    @Test
    public void testSchedulingMeetingOnTestCase5() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);
        meeting.setStanza(stanza);
        testExpectedResult(CLIENT_ERROR_STATUS, sendBadRequestMeeting(meeting,"2024-02-02 18,00",
                "2024-02-02 22:00"));

    }

    @Test
    public void testSchedulingMeetingOnTestCase6() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        meeting.setInizio(LocalDateTime.parse("2024-02-02 18:00",formatter));
        meeting.setFine(LocalDateTime.parse("2024-02-02 18:00",formatter));
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));

    }

    @Test
    public void testSchedulingMeetingOnTestCase7() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        meeting.setFine(LocalDateTime.parse("2024-02-02 16:00",formatter));
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));

    }

    @Test
    public void testSchedulingMeetingOnTestCase8() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        try{
            meeting.setFine(LocalDateTime.parse("2024-02-02 16:00",formatter));
        }catch(DateTimeParseException e){
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));
        }

    }

    @Test
    public void testSchedulingMeetingOnTestCase9() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        meeting.setInizio(LocalDateTime.now().minusDays(1));
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));

        meeting.setInizio(LocalDateTime.parse("2024-02-02 18:00",formatter));
        meeting.setFine(LocalDateTime.now().minusDays(1));

        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));

    }

    @Test
    public void testSchedulingMeetingOnTestCase10() throws Exception {

        try{
            meeting.setInizio(LocalDateTime.parse("2024-02-02 18,00",notCorrectFormatter));
        }catch(DateTimeParseException e){
            try{
                meeting.setFine(LocalDateTime.parse("2024-02-02 18,00",notCorrectFormatter));
            }catch(DateTimeParseException ex){
                testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));
            }
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestMeeting(meeting));
        }

    }

    @Test
    public void testCompilazioneQuestionarioOnSuccess(){

        valutazione = 5;
        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(anyString())).thenReturn(utente.getMetaId());

        try{
            //vedere i metodi private testExpectedResult e sendRequest
            testExpectedResult(SUCCESSFUL_STATUS, sendRequestQuestionario());

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testCompilazioneQuestionarioOnFailureToken(){

        valutazione = 5;
        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(false);

        try{
            //vedere i metodi private testExpectedResult e sendRequest
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestQuestionario());

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testCompilazioneQuestionarioOnFailureValueNotInsert(){

        valutazione = null;
        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        try{
            //vedere i metodi private testExpectedResult e sendRequest
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestQuestionario());

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    private String JSONConvertitorQuestionario(){
        //CREAAZIONE DEL BODY DELLA RICHIESTA
        //Converto l'istanza meeting in una stringa JSON accettabile del controller
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("valutazione", valutazione);

        return jsonNode.toString();
    }

    /**
     * metodo che prende un meeting e invia la richiesta al server
     * @retrun ResultActions
     * @throws Exception
     */
    private ResultActions sendRequestQuestionario() throws Exception {


        //formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL_Compilazione)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONConvertitorQuestionario())     //metodo privato della classe
                        .header("Authorization", "Bearer TODO");

        //ritorno della risposta
        return  MockMvcBuilders.standaloneSetup(meetingController)
                .build()
                .perform(requestBuilder);
    }

}
