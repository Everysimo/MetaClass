package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.MetaClass.exceptions.DataFormatException;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.controller.GestioneAmministrazioneController;
import com.commigo.metaclass.MetaClass.gestionestanza.controller.GestioneStanzaControl;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaServiceImpl;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import com.commigo.metaclass.MetaClass.utility.MapValidator;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = GestioneUtenzaController.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ComponentScan(basePackages = "com.commigo.metaclass.MetaClass")
public class GestioneUtenzaControllerUnitTest {
    private final String API_URL = "/modifyUserData";
    @InjectMocks
    private GestioneUtenzaController gestioneUtenzaController;
    @MockBean
    private ValidationToken validationToken;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private GestioneUtenzaService utenzaService;

    private Map<String, Object> requestData;
    private Utente utente;
    private HttpServletRequest request;
    private static final int CLIENT_ERROR_STATUS = 400;
    private static final int SUCCESSFUL_STATUS = 200;

    @BeforeEach
    public void setUp() throws Exception {
        utente = new Utente(1L, "Michele", "Pesce", "pescemichele@live.com",
                "05/30/1993","M","7184488154978627", Utente.DEFAULT_TOKEN, true);

        requestData = new HashMap<>();

    }

    /*private String JSONConvertitorUtente(Map<String, Object> utente){
        //CREAAZIONE DEL BODY DELLA RICHIESTA
        //Converto l'istanza meeting in una stringa JSON accettabile del controller
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();

        for()

        return jsonNode.toString();
    }

    /**
     * metodo che prende un meeting e invia la richiesta al server
     * @param scenario
     * @retrun ResultActions
     * @throws Exception
     */
   /* private ResultActions sendRequestScenario(Scenario scenario) throws Exception {


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
    */
    @Test
    void testModifyUserData() throws Exception {

        testModifyUserDataTestCases(1);
        testModifyUserDataTestCases(2);
        testModifyUserDataTestCases(3);
        testModifyUserDataTestCases(4);
        testModifyUserDataTestCases(5);
        testModifyUserDataTestCases(6);
        testModifyUserDataTestCases(7);
        testModifyUserDataTestCases(8);
        testModifyUserDataTestCases(9);
        testModifyUserDataTestCases(10);
        testModifyUserDataTestCases(11);


        // Mock your token validation result
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken())).thenReturn(utente.getMetaId()); // Mock your service method result
        when(utenzaService.modificaDatiUtente(Mockito.anyString(), Mockito.anyMap())).thenReturn(true);

        //converto la map
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(requestData);

        request = MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .header("Authorization", "Bearer TODO")
                .buildRequest(new MockServletContext());

        try{
            // Chiamata al metodo da testare
            ResponseEntity<Response<Boolean>> responseEntity =
                    gestioneUtenzaController.modifyUserData(requestData, request);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }


    }

    private void testModifyUserDataTestCases(int testcase) throws ClientRuntimeException {

        switch(testcase){
            case 1:
                requestData.put("nome", "S");
                assertValidationUser("Lunghezza nome errata");
                break;
            case 2:
                requestData.put("nome","S4lvator3");
                assertValidationUser("Formato nome errato");
                break;
            case 3:
                requestData.put("cognome","A");
                assertValidationUser("Lunghezza cognome errata");
                break;
            case 4:
                requestData.put("cognome", "Alb3ert1");
                assertValidationUser("Formato cognome errato");
                break;
            case 5:
                requestData.put("dataDiNascita","venti");
                assertValidationUser("Formato della data di nascita non valido. Formato richiesto: MM/dd/yyyy");
                break;
            case 6:
                requestData.put("sesso","MF");
                assertValidationUser("Lunghezza sesso non valida");
                break;
            case 7:
                requestData.put("sesso", "3");
                assertValidationUser("Il genere deve essere 'M', 'F' o 'O'");
                break;
            case 8:
                requestData.put("email","s.alberti1studenti.unisa.it");
                assertValidationUser("Formato email non valido");
                break;
            case 9:
                requestData.put("telefono","33659487");
                assertValidationUser("Lunghezza telefono non valida");
                break;
            case 10:
                requestData.put("telefono", "336agt9999");
                assertValidationUser("Formato telefono non valido");
                break;
            case 11:
                requestData.put("nome","Salvatore");
                requestData.put("cognome","Alberti");
                requestData.put("dataDiNascita","02/04/2003");
                requestData.put("sesso","M");
                requestData.put("email","s.alberti1@studenti.unisa.it");
                requestData.put("telefono","3365948795");
                boolean result = MapValidator.utenteValidate(requestData);
                assertTrue(result);
                break;
        }

            requestData.clear();
    }

    private void assertValidationUser(String message){
        // Verifica che l'eccezione sia stata lanciata
        ClientRuntimeException ex = assertThrows(ClientRuntimeException.class, () -> {
            MapValidator.utenteValidate(requestData);
        });
        assertEquals("Errore nella richiesta: "+message, ex.getMessage());
    }
}
