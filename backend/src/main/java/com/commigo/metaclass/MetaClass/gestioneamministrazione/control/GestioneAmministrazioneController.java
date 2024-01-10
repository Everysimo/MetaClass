package com.commigo.metaclass.MetaClass.gestioneamministrazione.control;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.service.GestioneAmministrazioneService;
import com.commigo.metaclass.MetaClass.gestionestanza.controller.GestioneStanzaControl;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class GestioneAmministrazioneController {

    @Autowired
    @Qualifier("GestioneAmministrazioneService")
    private GestioneAmministrazioneService gestioneamministrazione;

    @Autowired
    private ValidationToken validationToken;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private GestioneStanzaControl stanzaControl;

    private final Set<String> adminMetaIds = loadAdminMetaIdsFromFile();

    private Set<String> loadAdminMetaIdsFromFile() {
        Set<String> adminIds = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("admins.txt").getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                adminIds.add(line.trim());
            }
        } catch (IOException e) {
            // Gestione eccezioni legate alla lettura del file (ad esempio FileNotFoundException)
            e.printStackTrace();
        }
        return adminIds;
    }

    private boolean checkAdmin(String metaId){
        return adminMetaIds.contains(metaId);
    }

    @PostMapping(value = "/visualizzaUtentiBannatiInStanza/{Id}")
    public ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(@PathVariable Long Id, HttpServletRequest request) throws RuntimeException403 {
        try{
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            //verifica dei permessi
            if(!checkAdmin(metaID))  throw new RuntimeException403("Non sei amministratore");

            return stanzaControl.visualizzaUtentiBannatiInStanza(Id, request);

        }catch (RuntimeException403 re) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
        }
    }

    @PostMapping(value = "annullaBan/{idstanza}/{idUtente}")
    public ResponseEntity<Response<Boolean>> annullaBan(@PathVariable Long idUtente,
                                                        @PathVariable("idstanza") Long idStanza,
                                                        HttpServletRequest request)
    {
        try{
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            //verifica dei permessi
            if(!checkAdmin(metaID))  throw new RuntimeException403("non sei amministratore");

            gestioneamministrazione.deleteBanToUser(idUtente,idStanza);
            return ResponseEntity.ok(new Response<>(true,"Ban annullato correttamente"));

        }catch (RuntimeException403 re) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new Response<>(false, "Errore durante la richiesta: " + re.getMessage()));
       }catch(ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante la richiesta: " + se.getMessage()));
        }
    }


    @PostMapping(value = "updateCategoria")
    public ResponseEntity<Response<Boolean>> updateCategoria(@Valid @RequestBody Categoria c,
                                                             BindingResult result,
                                                             HttpServletRequest request) {
        try {

            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            //verifica dei permessi
            if(!checkAdmin(metaID))  throw new RuntimeException403("non sei amministratore");

            //controllo errori di validazione
            if(result.hasErrors())
            {
                throw new RuntimeException403(RequestUtils.errorsRequest(result));
            }

            if (!gestioneamministrazione.updateCategoria(c)) {
                throw new ServerRuntimeException("Errore durante l'inserimento della categoria");
            } else {
                return ResponseEntity.ok(new Response<>(true,
                        "categoria creata con successo"));
            }
        }catch(RuntimeException403 e){
            return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
        } catch (ServerRuntimeException e) {
            return ResponseEntity.status(500).body(new Response<>(false, e.getMessage()));
        }
    }

    @PostMapping(value = "updateScenario")
    public ResponseEntity<Response<Boolean>> updateScenario(@Valid @RequestBody Scenario s,
                                                            BindingResult result,
                                                            HttpServletRequest request){
        try {
            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            //verifica dei permessi
            if(!checkAdmin(metaID))  throw new RuntimeException403("accesso non consentito");


            //controllo errori di validazione
            if(result.hasErrors())
            {
                throw new RuntimeException403(RequestUtils.errorsRequest(result));
            }

            if (!gestioneamministrazione.updateScenario(s, s.getCategoria().getId())) {
                throw new ServerRuntimeException("Errore durante l'inserimento dello scenario");
            } else {
                return ResponseEntity.ok(new Response<>(true, "scenario creato con successo"));
            }
        }catch(RuntimeException403 e){
            return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
        }  catch (ServerRuntimeException e) {
            return ResponseEntity.status(500).body(new Response<>(false, e.getMessage()));
        }
    }

    @GetMapping(value = "allStanze")
    public ResponseEntity<Response<List<Stanza>>> visualizzaStanze(HttpServletRequest request) {
        List<Stanza> stanze;
        try {
            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            //verifica dei permessi
            if(!checkAdmin(metaID))  throw new RuntimeException403("accesso non consentito");


            stanze = gestioneamministrazione.getStanze();
            if(stanze == null){
                throw new ServerRuntimeException("Errore nella ricerca delle stanze");
            }else if(stanze.isEmpty()){
                return ResponseEntity
                        .ok(new Response<>(stanze, "nessuna stanza creata"));
            }else{
                return ResponseEntity
                        .ok(new Response<>(stanze, "operazione effettuata con successo"));
            }
        } catch (RuntimeException403 re) {
            return ResponseEntity.status(403)
                    .body(new Response<>(null, "Errore durante l'operazione: "+re.getMessage()));
        }catch (ServerRuntimeException e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "modificaStanza/{Id}")
    public ResponseEntity<Response<Boolean>> modifyRoomDataAdmin(
            @PathVariable Long Id,
            @RequestBody Map<String, Object> params,
            HttpServletRequest request){


       try{ //validazione dl token
          if (!validationToken.isTokenValid(request)) {
              throw new RuntimeException403("Token non valido");
          }

          String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

          //verifica dei permessi
          if(!checkAdmin(metaID))  throw new RuntimeException403("accesso non consentito");

          return stanzaControl.modifyRoomData(Id,params,request);
        } catch (RuntimeException403 re) {
           return ResponseEntity.status(403)
                   .body(new Response<>(null, "Errore durante l'operazione: "+re.getMessage()));
       }
    }

    @PostMapping(value = "eliminaStanza/{Id}")
    public ResponseEntity<Response<Boolean>> eliminaStanza(@PathVariable Long Id,
                                                           HttpServletRequest request){
        try{
            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            //verifica dei permessi
            if(!checkAdmin(metaID))  throw new RuntimeException403("accesso non consentito");

            return stanzaControl.eliminaStanza(Id,request);
        } catch (RuntimeException403 re) {
            return ResponseEntity.status(403)
                .body(new Response<>(false, "Errore durante l'operazione: "+re.getMessage()));
        }

   }

    @PostMapping(value = "visualizzaStanza/{Id}")
    public ResponseEntity<Response<Stanza>> visualizzaStanza(@PathVariable Long Id,
                                                             HttpServletRequest request) {
        try{
            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            //verifica dei permessi
            if(!checkAdmin(metaID))  throw new RuntimeException403("Non sei un amministratore");

            return stanzaControl.visualizzaStanza(Id,request);
        } catch (RuntimeException403 re) {
            return ResponseEntity.status(403)
                    .body(new Response<>(null, "Errore durante l'operazione: "+re.getMessage()));
        }
    }

}