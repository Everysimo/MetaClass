package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.MetaClass.exceptions.DataFormatException;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import com.commigo.metaclass.MetaClass.utility.MapValidator;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@PrepareForTest(MapValidator.class)
@ExtendWith(MockitoExtension.class)
public class GestioneUtenzaControllerUnitTest {
    private final String API_URL = "/modifyUserData";
    @InjectMocks
    private GestioneUtenzaController gestioneUtenzaController;
    @Mock
    private ValidationToken validationToken;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private GestioneUtenzaService utenzaService;

    private Map<String, Object> requestData;
    private Utente utente;
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() throws Exception {
        utente = new Utente(1L, "Michele", "Pesce", "pescemichele@live.com",
                "05/30/1993","M","7184488154978627", Utente.DEFAULT_TOKEN, true);

        requestData = new HashMap<>();

    }
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
