package com.commigo.metaclass.MetaClass.gestioneamministrazione.controller;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.service.GestioneAmministrazioneService;
import com.commigo.metaclass.MetaClass.gestionestanza.controller.GestioneStanzaControl;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaServiceImpl;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GestioneAmministrazioneController.class,
        GestioneStanzaControl.class,
        GestioneStanzaService.class,
        GestioneStanzaServiceImpl.class})
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ComponentScan(basePackages = "com.commigo.metaclass.MetaClass")
class GestioneAmministrazioneControllerUnitTest {

    /**
     *  Un mock oggetto è un oggetto simulato che può essere programmato per rispondere in modo specifico a chiamate di
     *  metodo durante i test.
     */
    @MockBean
    private GestioneAmministrazioneService amministrazioneService;
    @MockBean
    private GestioneStanzaService gestioneStanzaService;
    @MockBean
    private ValidationToken validationToken;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    private GestioneAmministrazioneController amministrazioneController;
    @InjectMocks
    private GestioneStanzaControl stanzaController;
    private Utente utente;
    private Immagine immagine;
    private Categoria categoria;
    private Scenario scenario;

    private static final String API_URL = "/admin/updateScenario";
    private static final int CLIENT_ERROR_STATUS = 400;
    private static final int SUCCESSFUL_STATUS = 200;
    private static final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom secureRandom = new SecureRandom();


    /**
     *  Questo metodo viene richiamato prima di ogni test e qui vanno inizializzate
     *  tutte le variabili che si andranno a utilizzare
     */
    @BeforeEach
    public void setUp() throws Exception {
        utente = new Utente(1L, "Michele", "Pesce", "pescemichele@live.com",
                "05/30/1993","M","7184488154978627", Utente.DEFAULT_TOKEN, true);
        immagine = new Immagine(1L, "lavoro1.txt","https://www.lavoro1.com/path/to/lavoro1.txt");
        categoria = new Categoria(1L, "Lavoro", "Categoria per il lavoro");
        scenario = new Scenario(1L, "Lavoro1", "Scenario 1 per il lavoro", immagine, categoria);

    }

    private String JSONConvertitorScenario(Scenario scenario){
        //CREAAZIONE DEL BODY DELLA RICHIESTA
        //Converto l'istanza meeting in una stringa JSON accettabile del controller
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Aggiunta degli attributi uno per uno
        jsonNode.put("nome", scenario.getNome());
        jsonNode.put("descrizione", scenario.getDescrizione());
        jsonNode.put("url_immagine", scenario.getImage().getUrl());
        jsonNode.put("id_categoria", scenario.getCategoria().getId());

        return jsonNode.toString();
    }

    /**
     * metodo che prende un meeting e invia la richiesta al server
     * @param scenario
     * @retrun ResultActions
     * @throws Exception
     */
    private ResultActions sendRequestScenario(Scenario scenario) throws Exception {


        //formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONConvertitorScenario(scenario))     //metodo privato della classe
                        .header("Authorization", "Bearer TODO");

        //ritorno della risposta
        return  MockMvcBuilders.standaloneSetup(amministrazioneController)
                .build()
                .perform(requestBuilder);
    }

    //metodo che produce una richiesta testa un errore 500 durante la richiesta
    private void sendRequestServerFailureScenario(Scenario scenario) throws Exception {
        // Formattamento della richiesta
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONConvertitorScenario(scenario))  // Metodo privato della classe
                .header("Authorization", "Bearer TODO");

        try {
            // Forza il metodo creaScheduling a lanciare un'eccezione ServerRuntimeException
            doThrow(ServerRuntimeException.class)
                    .when(amministrazioneService.updateScenario(any(), any()));

            // Esecuzione della richiesta e ritorno della risposta
            MvcResult result = MockMvcBuilders
                    .standaloneSetup(amministrazioneController)
                    .build()
                    .perform(requestBuilder)
                    .andReturn();

            // Verifica del codice di stato
            int statusCode = result.getResponse().getStatus();
            assertThat(statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        } finally {
            // Ripristina il comportamento normale del metodo creaScheduling dopo il test
            Mockito.reset(amministrazioneService);
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
     * testing creazione scenario
     * per ogni test case vado a richiamare un metodo che mi valida degli scenari volontariamente
     * errati per testare i messaggi di errori che vengono ritornati
     * successivamente si controlla ogni istruzione del metodo
     */
    @Test
    public void testUpdateScenarioOnSuccess(){

        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(amministrazioneController.checkAdmin(any(String.class))).thenReturn(true);

        try{
            when(amministrazioneService.updateScenario(scenario, categoria.getId())).thenReturn(true);
            // Chiamata al metodo da testare
            testExpectedResult(SUCCESSFUL_STATUS, sendRequestScenario(scenario));

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    @Test
    public void testUpdateScenarioOnAdminNotFound(){

        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(amministrazioneController.checkAdmin(any(String.class))).thenReturn(false);


        try{
            when(amministrazioneService.updateScenario(scenario, categoria.getId())).thenReturn(true);
            // Chiamata al metodo da testare
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestScenario(scenario));

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    @Test
    public void testUpdateScenarioOnTokenValidationFailed() throws Exception {

        when(validationToken.isTokenValid(any())).thenReturn(false);

        testExpectedResult(CLIENT_ERROR_STATUS, sendRequestScenario(scenario));

    }

    @Test
    public void testUpdateScenarioTestCase1(){
        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(amministrazioneController.checkAdmin(any(String.class))).thenReturn(true);

        scenario.setNome("U");
        try{
            when(amministrazioneService.updateScenario(scenario, categoria.getId())).thenReturn(true);
            // Chiamata al metodo da testare
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestScenario(scenario));

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    @Test
    public void testUpdateScenarioTestCase2(){
        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(amministrazioneController.checkAdmin(any(String.class))).thenReturn(true);

        scenario.setNome("ufficio");
        try{
            when(amministrazioneService.updateScenario(scenario, categoria.getId())).thenReturn(true);
            // Chiamata al metodo da testare
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestScenario(scenario));

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    @Test
    public void testUpdateScenarioTestCase3(){
        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(amministrazioneController.checkAdmin(any(String.class))).thenReturn(true);

        scenario.setNome("Ufficio");
        scenario.setDescrizione("");
        try{
            when(amministrazioneService.updateScenario(scenario, categoria.getId())).thenReturn(true);
            // Chiamata al metodo da testare
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestScenario(scenario));

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    @Test
    public void testUpdateScenarioTestCase4(){
        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(amministrazioneController.checkAdmin(any(String.class))).thenReturn(true);

        scenario.setDescrizione(generateRandomString(255));
        try{
            when(amministrazioneService.updateScenario(scenario, categoria.getId())).thenReturn(true);
            // Chiamata al metodo da testare
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestScenario(scenario));

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    @Test
    public void testUpdateScenarioTestCase5(){
        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        when(amministrazioneController.checkAdmin(any(String.class))).thenReturn(true);

        scenario.setDescrizione("1Questo scenario rappresenta un ufficio di lavoro");

        try{
            when(amministrazioneService.updateScenario(scenario, categoria.getId())).thenReturn(true);
            // Chiamata al metodo da testare
            testExpectedResult(CLIENT_ERROR_STATUS, sendRequestScenario(scenario));

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }

    private String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(UPPER_CASE_CHARS.length());
            char randomChar = UPPER_CASE_CHARS.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

}
