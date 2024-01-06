package com.commigo.metaclass.MetaClass.gestioneamministrazione.control;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.service.GestioneAmministrazioneService;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GestioneAmministrazioneController {

    @Autowired
    @Qualifier("GestioneAmministrazioneService")
    private GestioneAmministrazioneService gestioneamministrazione;

    @Autowired
    private ValidationToken validationToken;

    @PostMapping(value = "admin/annullaBan/{idstanza}")
    public ResponseEntity<Response<Boolean>> annullaBan(@RequestBody String idUtente, @PathVariable("idstanza") Long idStanza)
    {
        Utente utente = gestioneamministrazione.findUtenteById(idUtente);
        Stanza stanza = gestioneamministrazione.findStanzaById(idStanza);

        if(!gestioneamministrazione.isBannedUser(utente,stanza))
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(false,"Utente non bannato"));
        }

        return ResponseEntity.ok(new Response<>(true,"Ban annullato correttamente"));

    }

    @PostMapping(value = "admin/updateCategoria")
    public ResponseEntity<Response<Boolean>> updateCategoria(@RequestBody Categoria c,
                                                             HttpServletRequest request,
                                                             BindingResult result) {
        try {

            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            //controllo errori di validazione
            if(result.hasErrors())
            {
                throw new RuntimeException403(RequestUtils.errorsRequest(result));
            }

            if (!gestioneamministrazione.updateCategoria(c)) {
                throw new Exception("Errore durante l'inserimento della categoria");
            } else {
                return ResponseEntity.ok(new Response<>(true,
                        "categoria creata con successo"));
            }
        }catch(RuntimeException403 e){
            return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(false, e.getMessage()));
        }
    }

    @PostMapping(value = "admin/updateScenario")
    public ResponseEntity<Response<Boolean>> updateScenario(@RequestBody Scenario s,
                                                             HttpServletRequest request,
                                                             BindingResult result) {
        try {
            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            //controllo errori di validazione
            if(result.hasErrors())
            {
                throw new RuntimeException403(RequestUtils.errorsRequest(result));
            }

            if (!gestioneamministrazione.updateScenario(s, s.getCategoria().getId())) {
                throw new Exception("Errore durante l'inserimento dello scenario");
            } else {
                return ResponseEntity.ok(new Response<>(true, "scenario creato con successo"));
            }
        }catch(RuntimeException403 e){
            return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
        }  catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(false, e.getMessage()));
        }
    }

    @GetMapping(value = "admin/allStanze")
    public ResponseEntity<Response<List<Stanza>>> visualizzaStanze() {
        List<Stanza> stanze;
        try {
            stanze = gestioneamministrazione.getStanze();
            if(stanze == null){
                return ResponseEntity.status(500)
                        .body(new Response<>(null, "Errore la ricerca delle stanze"));
            }else if(stanze.isEmpty()){
                return ResponseEntity
                        .ok(new Response<>(stanze, "nessuna stanza creata"));
            }else{
                return ResponseEntity
                        .ok(new Response<>(stanze, "operazione effettuata con successo"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, "Errore durante l'operazione"));
        }
    }
}