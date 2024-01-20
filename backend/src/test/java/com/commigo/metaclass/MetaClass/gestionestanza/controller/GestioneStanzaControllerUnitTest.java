package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.CustomExceptionHandler;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.MetaClass.utility.response.types.AccessResponse;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes ={GestioneStanzaControl.class,CustomExceptionHandler.class})
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GestioneStanzaControllerUnitTest {

    /**
     *  Un mock oggetto è un oggetto simulato che può essere programmato per rispondere in modo specifico a chiamate di
     *  metodo durante i test.
     */
    @MockBean
    private GestioneStanzaService stanzaService;
    @MockBean
    private ValidationToken validationToken;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private StanzaRepository stanzaRepository;
    @InjectMocks
    private GestioneStanzaControl stanzaController;
    private static final int CLIENT_ERROR_STATUS = 400;
    private static final int SUCCESSFUL_STATUS = 200;
    private Stanza stanza;
    private Utente utente;
    private Meeting meeting;
    private Ruolo organizzatore_master;
    private Immagine immagine;
    private Categoria categoria;
    private Scenario scenario;
    private DateTimeFormatter formatter;
    private final String API_URL_CREASTANZA = "/creastanza";
    private final String API_URL_ACCESOSTANZA = "/accessoStanza";


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
    }

    private String JSONConvertitorStanza(Stanza stanza){
        //CREAAZIONE DEL BODY DELLA RICHIESTA
        //Converto l'istanza meeting in una stringa JSON accettabile del controller
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("nome", stanza.getNome());
        jsonNode.put("descrizione", stanza.getDescrizione());
        jsonNode.put("tipoAccesso", stanza.isTipo_Accesso());
        jsonNode.put("maxPosti", stanza.getMax_Posti());
        jsonNode.put("id_scenario", stanza.getScenario().getId());

        return jsonNode.toString();
    }

    /**
     * metodo che prende una stanza e invia la richiesta al server
     * @param stanza
     * @retrun ResultActions
     * @throws Exception
     */
    private ResultActions sendRequestStanza(Stanza stanza) throws Exception {


        //formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL_CREASTANZA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONConvertitorStanza(stanza))     //metodo privato della classe
                        .header("Authorization", "Bearer TODO");

        //ritorno della risposta
        return  MockMvcBuilders.standaloneSetup(stanzaController)
                .build()
                .perform(requestBuilder);
    }

    //metodo che produce una richiesta testa un errore 500 durante la richiesta
    private void sendRequestFailureAccessoStanza(Stanza stanza, Class<? extends Exception> exceptionClass, int status) throws Exception {
        // Formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_URL_CREASTANZA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONConvertitorStanza(stanza))  // Metodo privato della classe
                .header("Authorization", "Bearer TODO");

        try {
            // Forza il metodo creaStanza a lanciare un'eccezione ServerRuntimeException
            doThrow(exceptionClass)
                    .when(stanzaService).creaStanza(any(), any());

            // Esecuzione della richiesta e ritorno della risposta
            MvcResult result = MockMvcBuilders
                    .standaloneSetup(stanzaController)
                    .build()
                    .perform(requestBuilder)
                    .andReturn();

            // Verifica del codice di stato
            int statusCode = result.getResponse().getStatus();
            assertThat(statusCode).isEqualTo(status);

        } finally {
            // Ripristina il comportamento normale del metodo creaScheduling dopo il test
            Mockito.reset(stanzaService);
        }
    }

    private void sendRequestFailureCreaStanza(Stanza stanza, Class<? extends Exception> exceptionClass, int status) throws Exception {
        // Formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_URL_CREASTANZA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONConvertitorStanza(stanza))  // Metodo privato della classe
                .header("Authorization", "Bearer TODO");

        try {
            // Forza il metodo creaStanza a lanciare un'eccezione ServerRuntimeException
            doThrow(exceptionClass)
                    .when(stanzaService).creaStanza(any(), any());

            // Esecuzione della richiesta e ritorno della risposta
            MvcResult result = MockMvcBuilders
                    .standaloneSetup(stanzaController)
                    .build()
                    .perform(requestBuilder)
                    .andReturn();

            // Verifica del codice di stato
            int statusCode = result.getResponse().getStatus();
            assertThat(statusCode).isEqualTo(status);

        } finally {
            // Ripristina il comportamento normale del metodo creaScheduling dopo il test
            Mockito.reset(stanzaService);
        }
    }


    private void testExpectedResult(int status, ResultActions actualPerformResult) throws Exception {

        MvcResult mvcResult = actualPerformResult.andReturn();
        int resultStatus = mvcResult.getResponse().getStatus();
        String responseContent = mvcResult.getResponse().getContentAsString();

        System.out.println("Actual Status: " + resultStatus);
        System.out.println("Response Content: " + responseContent);

        if(status==CLIENT_ERROR_STATUS)
            actualPerformResult.andExpect(status().is4xxClientError());
        else if(status==SUCCESSFUL_STATUS)
            actualPerformResult.andExpect(status().is2xxSuccessful());

    }

    /**
     * testing crea stanza
     * per ogni test case vado a richiamare un metodo che valida delle stanze volontariamente
     * errate per testare i messaggi di errori che vengono ritornati
     * successivamente si controlla ogni istruzione del metodo
     */
    @Test
    public void testCreaStanzaOnSuccess(){

        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);
        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        try{
            when(stanzaService.creaStanza(stanza, utente.getMetaId())).thenReturn(true);

            //vedere i metodi private testExpectedResult e sendRequest
            testExpectedResult(SUCCESSFUL_STATUS, sendRequestStanza(stanza));

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    @Test
    public void testCreaStanzaOnTokenValidationFailed() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(false);

        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestStanza(stanza));

    }

    @Test
    public void testCreaStanzaOnServerFailure() throws Exception {
        when(validationToken.isTokenValid(any())).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(stanzaService.creaStanza(stanza, utente.getMetaId()))
                .thenThrow(ServerRuntimeException.class);

        //vedere i metodi private testExpectedResult e sendRequest
        sendRequestFailureCreaStanza(stanza, ServerRuntimeException.class, 500);
    }

    @Test
    public void testCreaStanzaFailure() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(stanzaService.accessoStanza(stanza.getCodice(), utente.getMetaId()))
                .thenThrow(ServerRuntimeException.class);

        //vedere i metodi private testExpectedResult e sendRequest
        sendRequestFailureCreaStanza(stanza, Exception.class, 500);
    }

    @Test
    public void testCreaStanzaOnTestCase1() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        stanza.setNome("C");
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestStanza(stanza));

    }

    @Test
    public void testCreaStanzaOnTestCase2() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        stanza.setNome("1Stanza");
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestStanza(stanza));

    }

    @Test
    public void testCreaStanzaOnTestCase5() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);
        stanza.setDescrizione("");
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestStanza(stanza));
    }

    @Test
    public void testCreaStanzaOnTestCase6() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        stanza.setDescrizione("Una stanza virtuale luminosa e moderna, con pareti bianche e una grande finestra virtuale che offre una vista su un cielo azzurro. Al centro, un tavolo digitale circondato da sedie ergonomiche, mentre schermi fluttuanti mostrano presentazioni e volti sorridenti dei partecipanti. Ambientazione accogliente e tecnologica, perfetta per incontri online produttivi.");
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestStanza(stanza));
    }

    @Test
    public void testCreaStanzaOnTestCase7() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        stanza.setDescrizione("questa stanza funge da esempio.");
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestStanza(stanza));

    }

    @Test
    public void testCreaStanzaOnTestCase9() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        stanza.setMax_Posti(0);
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestStanza(stanza));

    }

    @Test
    public void testCreaStanzaOnTestCase10() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        stanza.setMax_Posti(1000);
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestStanza(stanza));
    }

    @Test
    public void testCreaStanzaOnTestCase11() throws Exception {

        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ObjectNode jsonNode = objectMapper.createObjectNode();

            // Aggiunta degli attributi uno per uno
            jsonNode.put("nome", stanza.getNome());
            jsonNode.put("descrizione", stanza.getDescrizione());
            jsonNode.put("tipoAccesso", stanza.isTipo_Accesso());
            jsonNode.put("maxPosti", "A");
            jsonNode.put("id_scenario", stanza.getScenario().getId());


            //formattamento della richiesta
            MockHttpServletRequestBuilder requestBuilder =
                    MockMvcRequestBuilders.post(API_URL_CREASTANZA)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonNode.toString())
                            .header("Authorization", "Bearer TODO");

            //ritorno della risposta
            ResultActions ra = MockMvcBuilders.standaloneSetup(stanzaController)
                    .build()
                    .perform(requestBuilder);

            testExpectedResult(CLIENT_ERROR_STATUS, ra);

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    @Test
    public void testCreaStanzaOnTestCase12() throws Exception {

        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ObjectNode jsonNode = objectMapper.createObjectNode();

            // Aggiunta degli attributi uno per uno
            jsonNode.put("nome", stanza.getNome());
            jsonNode.put("descrizione", stanza.getDescrizione());
            jsonNode.put("tipoAccesso", stanza.isTipo_Accesso());
            jsonNode.put("maxPosti", stanza.getMax_Posti());
            jsonNode.put("id_scenario", (JsonNode) null);


            //formattamento della richiesta
            MockHttpServletRequestBuilder requestBuilder =
                    MockMvcRequestBuilders.post(API_URL_CREASTANZA)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonNode.toString())
                            .header("Authorization", "Bearer TODO");

            //ritorno della risposta
            ResultActions ra = MockMvcBuilders.standaloneSetup(stanzaController)
                    .build()
                    .perform(requestBuilder);

            testExpectedResult(CLIENT_ERROR_STATUS, ra);

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    private String JSONConvertitorCodice(String codice){
        //CREAAZIONE DEL BODY DELLA RICHIESTA
        //Converto l'istanza meeting in una stringa JSON accettabile del controller
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("codice", codice);

        return jsonNode.toString();
    }
    /**
     * testing accesso stanza
     * per ogni test case vado a richiamare un metodo che valida delle stanze volontariamente
     * errate per testare i messaggi di errori che vengono ritornati
     * successivamente si controlla ogni istruzione del metodo
     */
    @Test
    public void testRichiestaAccessoStanzaOnSuccess(){

        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        try{
            when(stanzaService.accessoStanza(any(), any()))
                    .thenReturn(ResponseEntity.ok(any(AccessResponse.class)));

            //vedere i metodi private testExpectedResult e sendRequest
            testExpectedResult(SUCCESSFUL_STATUS, sendRequestStanza(stanza));

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    private ResultActions sendRequestAccessoStanza(Stanza stanza) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("codice", stanza.getCodice());
        System.out.println(jsonNode.toString());
        //formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL_ACCESOSTANZA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNode.toString())     //metodo privato della classe
                        .header("Authorization", "Bearer TODO");

        //ritorno della risposta
        return  MockMvcBuilders.standaloneSetup(stanzaController)
                .build()
                .perform(requestBuilder);
    }

    @Test
    public void testAccessoStanzaOnTokenValidationFailed() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(false);

        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestAccessoStanza(stanza));

    }

    @Test
    public void testAccessoStanzaOnJsonProcessingFailure() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(stanzaService.accessoStanza(stanza.getCodice(), utente.getMetaId()))
                .thenThrow(JsonProcessingException.class);

        //vedere i metodi private testExpectedResult e sendRequest
        sendRequestFailureAccessoStanza(stanza, JsonProcessingException.class, 403);
    }

    @Test
    public void testAccessoStanzaOnServerFailure() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(stanzaService.accessoStanza(stanza.getCodice(), utente.getMetaId()))
                .thenThrow(ServerRuntimeException.class);

        //vedere i metodi private testExpectedResult e sendRequest
        sendRequestFailureAccessoStanza(stanza, ServerRuntimeException.class, 500);
    }

    @Test
    public void testAccessoStanzaAttributeNotValid() throws Exception{

        when(validationToken.isTokenValid(any())).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("code", 654321);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL_ACCESOSTANZA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNode.toString())
                        .header("Authorization", "Bearer TODO");

        //ritorno della risposta
        ResultActions ra = MockMvcBuilders.standaloneSetup(stanzaController)
                .build()
                .perform(requestBuilder);

        testExpectedResult(CLIENT_ERROR_STATUS, ra);
    }

    @Test
    public void testAccessoStanzaCodeisTextual() throws Exception{
        when(validationToken.isTokenValid(any())).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("codice", true);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL_ACCESOSTANZA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNode.toString())
                        .header("Authorization", "Bearer TODO");

        //ritorno della risposta
        ResultActions ra = MockMvcBuilders.standaloneSetup(stanzaController)
                .build()
                .perform(requestBuilder);

        testExpectedResult(CLIENT_ERROR_STATUS, ra);
    }

    @Test
    public void testAccessoStanzaOnTestCase1() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(true);

        stanza.setCodice("1234");
        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestAccessoStanza(stanza));

    }
}