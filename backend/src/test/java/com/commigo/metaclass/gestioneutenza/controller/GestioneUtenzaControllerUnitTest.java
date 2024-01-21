package com.commigo.metaclass.gestioneutenza.controller;

import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.gestioneutenza.service.GestioneUtenzaService;
import com.commigo.metaclass.utility.MapValidator;
import com.commigo.metaclass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.webconfig.ValidationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = GestioneUtenzaController.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ComponentScan(basePackages = "com.commigo.metaclass.MetaClass")
@PrepareForTest({MapValidator.class})
class GestioneUtenzaControllerUnitTest {
  private final String API_URL = "/modifyUserData";
  @InjectMocks private GestioneUtenzaController gestioneUtenzaController;
  @MockBean private ValidationToken validationToken;
  @MockBean private JwtTokenUtil jwtTokenUtil;

  @MockBean private GestioneUtenzaService utenzaService;

  private Map<String, Object> requestData;
  private Utente utente;
  private HttpServletRequest request;
  private static final int CLIENT_ERROR_STATUS = 400;
  private static final int SUCCESSFUL_STATUS = 200;

  @BeforeEach
  public void setUp() throws Exception {
    utente =
        new Utente(
            1L,
            "Michele",
            "Pesce",
            "pescemichele@live.com",
            "05/30/1993",
            "M",
            "7184488154978627",
            Utente.DEFAULT_TOKEN,
            true);

    requestData = new HashMap<>();
  }

  private String JSONConvertitorUtente(Map<String, Object> utente) {
    // Creazione del body della richiesta
    // Converto l'istanza meeting in una stringa JSON accettabile del controller
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode jsonNode = objectMapper.createObjectNode();

    for (Map.Entry<String, Object> entry : utente.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      // Aggiungo la coppia chiave-valore al JSON
      if (value != null) {
        // Aggiungo il valore solo se non è nullo
        if (value instanceof String) {
          jsonNode.put(key, (String) value);
        } else if (value instanceof Number) {
          jsonNode.put(key, (Short) value);
        } else if (value instanceof Boolean) {
          jsonNode.put(key, (Boolean) value);
        } else {
          // Se il tipo non è uno dei tipi primitivi gestiti da Jackson, converto a JSON usando
          // Jackson
          jsonNode.set(key, objectMapper.valueToTree(value));
        }
      }
    }

    return jsonNode.toString();
  }

  /**
   * metodo che prende un meeting e invia la richiesta al server
   *
   * @retrun ResultActions
   * @throws Exception
   */
  private ResultActions sendRequestUtente() throws Exception {

    // formattamento della richiesta
    MockHttpServletRequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSONConvertitorUtente(requestData)) // metodo privato della classe
            .header("Authorization", "Bearer TODO");

    // ritorno della risposta
    return MockMvcBuilders.standaloneSetup(gestioneUtenzaController)
        .build()
        .perform(requestBuilder);
  }

  // metodo che produce una richiesta testa un errore 400 durante la richiesta
  private void sendRequestClientFailureUtente() throws Exception {
    // Formattamento della richiesta
    MockHttpServletRequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSONConvertitorUtente(requestData)) // Metodo privato della classe
            .header("Authorization", "Bearer TODO");

    try {
      // Forza il metodo creaScheduling a lanciare un'eccezione ServerRuntimeException
      Mockito.doThrow(ClientRuntimeException.class).when(utenzaService.modificaDatiUtente(any(), any()));

      // Esecuzione della richiesta e ritorno della risposta
      MvcResult result =
          MockMvcBuilders.standaloneSetup(gestioneUtenzaController)
              .build()
              .perform(requestBuilder)
              .andReturn();

      // Verifica del codice di stato
      int statusCode = result.getResponse().getStatus();
      assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());

    } finally {
      // Ripristina il comportamento normale del metodo creaScheduling dopo il test
      Mockito.reset(utenzaService);
    }
  }

  private void testExpectedResult(int status, ResultActions actualPerformResult) throws Exception {

    MvcResult mvcResult = actualPerformResult.andReturn();
    int resultStatus = mvcResult.getResponse().getStatus();
    String responseContent = mvcResult.getResponse().getContentAsString();

    System.out.println("Actual Status: " + resultStatus);
    System.out.println("Response Content: " + responseContent);

    if (status == CLIENT_ERROR_STATUS) actualPerformResult.andExpect(status().is4xxClientError());
    else if (status == SUCCESSFUL_STATUS) actualPerformResult.andExpect(status().is2xxSuccessful());
  }

  @Test
  void testModifyUserDataOnSuccess() throws Exception {

    // Mock your token validation result
    when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);
    when(jwtTokenUtil.getmetaIdFromToken(validationToken.getToken()))
        .thenReturn(utente.getmetaId()); // Mock your service method result
    when(utenzaService.modificaDatiUtente(Mockito.anyString(), Mockito.anyMap())).thenReturn(true);

    try {
      // vedere i metodi private testExpectedResult e sendRequest
      testExpectedResult(SUCCESSFUL_STATUS, sendRequestUtente());

    } catch (Exception e) {
      fail("Exception not expected: " + e.getMessage());
    }
  }

  @Test
  public void testSchedulingMeetingOnTokenValidationFailed() throws Exception {

    when(validationToken.isTokenValid(any())).thenReturn(false);

    testExpectedResult(CLIENT_ERROR_STATUS, sendRequestUtente());
  }

  @Test
  public void testSchedulingMeetingOnTestCase1() throws Exception {

    requestData.put("nome", "S");
    when(validationToken.isTokenValid(any())).thenReturn(true);

    testExpectedResult(CLIENT_ERROR_STATUS, sendRequestUtente());
  }

  @Test
  public void testSchedulingMeetingOnTestCase2() throws Exception {

    requestData.put("nome", "S4lvator3");
    when(validationToken.isTokenValid(any())).thenReturn(true);

    testExpectedResult(CLIENT_ERROR_STATUS, sendRequestUtente());
  }

  @Test
  public void testSchedulingMeetingOnTestCase3() throws Exception {

    requestData.put("cognome", "A");

    when(validationToken.isTokenValid(any())).thenReturn(true);
    testExpectedResult(CLIENT_ERROR_STATUS, sendRequestUtente());
  }

  @Test
  public void testSchedulingMeetingOnTestCase4() throws Exception {

    requestData.put("cognome", "Alb3ert1");
    when(validationToken.isTokenValid(any())).thenReturn(true);

    testExpectedResult(CLIENT_ERROR_STATUS, sendRequestUtente());
  }

  @Test
  public void testSchedulingMeetingOnTestCase5() throws Exception {

    requestData.put("email", "s.alberti1studenti.unisa.it");
    when(validationToken.isTokenValid(any())).thenReturn(true);

    testExpectedResult(CLIENT_ERROR_STATUS, sendRequestUtente());
  }

  @Test
  public void testSchedulingMeetingOnTestCase6() throws Exception {

    requestData.put("telefono", "33659487");
    when(validationToken.isTokenValid(any())).thenReturn(true);

    testExpectedResult(CLIENT_ERROR_STATUS, sendRequestUtente());
  }

  @Test
  public void testSchedulingMeetingOnTestCase7() throws Exception {

    requestData.put("telefono", "336agt9999");
    when(validationToken.isTokenValid(any())).thenReturn(true);

    testExpectedResult(CLIENT_ERROR_STATUS, sendRequestUtente());
  }

  @Test
  public void testSchedulingMeetingOnTestCase8() throws Exception {

    requestData.put("dataDiNascita", "12-12-2001");
    when(validationToken.isTokenValid(any())).thenReturn(true);

    testExpectedResult(CLIENT_ERROR_STATUS, sendRequestUtente());
  }

  @Test
  public void testSchedulingMeetingOnTestCase9() throws Exception {

    requestData.put("nome", "Salvatore");
    requestData.put("cognome", "Alberti");
    requestData.put("dataDiNascita", "11/30/2001");
    requestData.put("email", "s.alberti1@studenti.unisa.it");
    requestData.put("telefono", "3365948795");

    when(validationToken.isTokenValid(any())).thenReturn(true);

    testExpectedResult(SUCCESSFUL_STATUS, sendRequestUtente());
  }
}
