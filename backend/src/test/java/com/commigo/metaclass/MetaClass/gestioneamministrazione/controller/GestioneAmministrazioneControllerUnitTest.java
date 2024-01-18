package com.commigo.metaclass.MetaClass.gestioneamministrazione.controller;

import com.commigo.metaclass.MetaClass.MetaClassApplicationTests;
import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.control.GestioneAmministrazioneController;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.service.GestioneAmministrazioneService;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MetaClassApplicationTests.class)
@ExtendWith(MockitoExtension.class)
public class GestioneAmministrazioneControllerUnitTest {

    /**
     *  Un mock oggetto è un oggetto simulato che può essere programmato per rispondere in modo specifico a chiamate di
     *  metodo durante i test.
     */
    @Mock
    private GestioneAmministrazioneService amministrazioneService;
    @Mock
    private ValidationToken validationToken;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    private GestioneAmministrazioneController amministrazioneController;
    private Utente utente;
    private Immagine immagine;
    private Categoria categoria;
    private Scenario scenario;
    private HttpServletRequest request;
    private ValidatorFactory validatorFactory;
    private Validator validator;
    private BindingResult bindingResult;


    /**
     *  Questo metodo viene richiamato prima di ogni test e qui vanno inizializzate
     *  tutte le variabili che si andranno ad utilizzare
     */
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        utente = new Utente(1L, "Michele", "Pesce", "pescemichele@live.com",
                "05/30/1993","M","7184488154978627", Utente.DEFAULT_TOKEN, true);
        immagine = new Immagine(1L, "lavoro1.txt","https://www.lavoro1.com/path/to/lavoro1.txt");
        categoria = new Categoria(1L, "Lavoro", "Categoria per il lavoro");
        scenario = new Scenario(1L, "Lavoro1", "Scenario 1 per il lavoro", immagine, categoria);
        request = MockMvcRequestBuilders
                .post("/admin/updateScenario")  // Assicurati di utilizzare il percorso corretto
                .header("Authorization", "Bearer TODO")
                .buildRequest(new MockServletContext());

    }

    /**
     * testing creazione scenario
     * per ogni test case vado a richiamare un metodo che mi valida degli scenario volontariamente
     * errati per testare i messaggi di errori che vengono ritornati
     * successivamente si controlla ogni istruzione del metodo
     */
    @Test
    public void testUpdateScenario(){

        //test case 5.1.1
        testCasesUpdateScenario(1);
        //test case 5.1.2
        testCasesUpdateScenario(2);
        //test case 5.1.3
        testCasesUpdateScenario(3);
        //test case 5.1.4
        testCasesUpdateScenario(4);
        //test case 5.1.5
        testCasesUpdateScenario(5);
        //test case 5.1.6
        testCasesUpdateScenario(6);

        // Simula un token valido
        when(validationToken.isTokenValid(any(HttpServletRequest.class))).thenReturn(true);

        // Simula la decodifica del token e restituisce un metaID valido
        when(jwtTokenUtil.getMetaIdFromToken(validationToken.getToken()))
                .thenReturn(utente.getMetaId());

        /*when(amministrazioneController.checkAdmin(utente.getMetaId()))
                .thenReturn(true);*/

        try{
            when(amministrazioneService.updateScenario(scenario, categoria.getId())).thenReturn(true);
            // Chiamata al metodo da testare
            ResponseEntity<Response<Boolean>> responseEntity =
                    amministrazioneController.updateScenario(scenario, bindingResult, request);

            if(!bindingResult.hasErrors())
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            else
                assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }

    }
    @Test
    private void testCasesUpdateScenario(int testcase){

        switch (testcase){
            case 1:
                scenario.setNome("U");
                assertValidationScenario("Lunghezza del nome non valida");
                break;
            case 2:
                scenario.setNome("ufficio");
                assertValidationScenario("Formato nome errato");
                break;
            case 3:
                scenario.setDescrizione(null);
                assertValidationScenario("La descrizione non può essere nulla");
                break;
            case 4:
                scenario.setDescrizione(generateRandomString(255));
                assertValidationScenario("Lunghezza della descrizione non valida");
                break;
            case 5:
                scenario.setDescrizione("1Questo scenario rappresenta un ufficio di lavoro.");
                assertValidationScenario("Formato descrizione errato");
                break;
            case 6:
                applyValidation();
                assertFalse(bindingResult.hasErrors());
        }
        scenario = new Scenario(1L, "Lavoro1", "Scenario 1 per il lavoro", immagine, categoria);

    }

    private void applyValidation(){
        validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .buildValidatorFactory();

        validator = validatorFactory.getValidator();

        // Esegui la validazione
        Set<ConstraintViolation<Scenario>> violations = validator.validate(scenario);
        bindingResult = new BeanPropertyBindingResult(scenario, "scenario");
        for (ConstraintViolation<Scenario> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            bindingResult.addError(new FieldError("meeting", propertyPath, message));
        }
    }

    private void assertValidationScenario(String message){
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

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

}
