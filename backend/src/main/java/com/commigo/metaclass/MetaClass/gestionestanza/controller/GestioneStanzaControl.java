package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException401;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.MetaClass.utility.request.GestioneAccessiRequest;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.request.RichiestaDTO;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.AccessResponse;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class GestioneStanzaControl {

    @Autowired
    @Qualifier("GestioneStanzaService")
    private GestioneStanzaService stanzaService;

    @Autowired
    private ValidationToken validationToken;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping(value = "/creastanza")
    public ResponseEntity<Response<Boolean>> creaStanza(@RequestBody Stanza s,
                                                        BindingResult result,
                                                        HttpServletRequest request){

        try
        {
            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            if(result.hasErrors())
            {
                return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR, RequestUtils.errorsRequest(result));
            }

            if(!stanzaService.creaStanza(s)){
                throw new ServerRuntimeException("errore nel salvataggio dell stanza");
            }
            return ResponseUtils.getResponseOk("Corretto");


        }catch (ServerRuntimeException e)
        {
            return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,"Errore durante la richiesta: " + e.getMessage());
        }catch(RuntimeException403 se){
            return ResponseUtils.getResponseError(HttpStatus.valueOf(403),
                    "Errore durante la richiesta: " + se.getMessage());
        }
    }

    @PostMapping(value = "/declassaOrganizzatore")
    public ResponseEntity<Response<Boolean>> declassaOrganizzatore(@RequestBody RichiestaDTO richiesta, HttpServletRequest request) {
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            return ResponseEntity.ok(stanzaService.downgradeUtente(metaID, richiesta.getId_og(), richiesta.getId_stanza()));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "/eliminaStanza/{Id}")

    public ResponseEntity<Response<Boolean>> eliminaStanza(@PathVariable Long Id, HttpServletRequest request) {
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            return ResponseEntity.ok(stanzaService.deleteRoom(metaID, Id));

        } catch (RuntimeException403 re) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, "Errore durante l'operazione: "+re.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "/gestioneAccessi")
    public ResponseEntity<Response<List<StatoPartecipazione>>> gestioneAccessi(@RequestBody GestioneAccessiRequest request, HttpSession session, BindingResult result)
    {

        if(result.hasErrors())
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(null,RequestUtils.errorsRequest(result)));
        }

        Stanza stanza = stanzaService.findStanza(request.getIdstanza());
        if(stanza == null)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(null,"Id stanza non valido"));
        }

        List<StatoPartecipazione> list = stanzaService.findStatoPartecipazioniInAttesa(stanza,true);

        if(request.isAccept())
        {
            return ResponseEntity.ok(new Response<>(list.stream().filter((sp) -> sp.getUtente().getId() == request.getIdutente()).toList(),
                    "Utente a cui è stata accettata la richiesta"));
        }

        return ResponseEntity.ok(new Response<>(list,"Lista stati partecipazioni in attesa della stanza"));

    }

    @PostMapping(value = "/modifyRoomData/{Id}")
    public ResponseEntity<Response<Boolean>> modifyRoomData(
            @PathVariable Long Id,
            @Valid @RequestBody Stanza s,
            BindingResult result,
            HttpServletRequest request) {

        try {
                //controllo del token
                if (!validationToken.isTokenValid(request)) {
                    throw new RuntimeException403("Token non valido");
                }

                //controllo errori di validazione
                if(result.hasErrors()) {
                     throw new RuntimeException403(RequestUtils.errorsRequest(result));
                }

                if(!stanzaService.modificaDatiStanza(s,Id)){
                    throw new ServerRuntimeException("modifica non effettuata");
                } else {
                    return ResponseEntity.ok(new Response<>(true, "Stanza modificata con successo"));
                }

        } catch(RuntimeException403 re) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, "Errore durante l'operazione: "+re.getMessage()));
        }catch(RuntimeException401 ue) {
            return ResponseEntity.status(401)
                .body(new Response<>(false, "Errore durante l'operazione: "+ue.getMessage()));
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione"));
        }

    }

    @PostMapping(value = "/promuoviOrganizzatore")
    public ResponseEntity<Response<Boolean>> promuoviOrganizzatore(@RequestBody RichiestaDTO richiesta, HttpServletRequest request) {
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());
                return ResponseEntity.ok(stanzaService.upgradeUtente(metaID, richiesta.getId_og(), richiesta.getId_stanza()));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "/accessoStanza")
    public ResponseEntity<AccessResponse<Boolean>> richiestaAccessoStanza(@RequestBody String requestBody, HttpServletRequest request)
    {
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            String codiceStanza = jsonNode.get("codice").asText();

            return ResponseEntity.ok(stanzaService.accessoStanza(codiceStanza, metaID).getBody());

        } catch (RuntimeException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AccessResponse<>(false, "Errore durante la richiesta: " + e.getMessage(), false));
        } catch (RuntimeException403 e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/visualizzaUtentiInStanza/{Id}")
    public List<Utente> visualizzaUtentiInStanza(@PathVariable Long Id, HttpServletRequest request) throws RuntimeException403 {

        if (!validationToken.isTokenValid(request)) {
            throw new RuntimeException403("Token non valido");
        }

        return stanzaService.visualizzaUtentiInStanza(Id);
    }

    @PostMapping(value = "/visualizzaStanza/{Id}")
    public ResponseEntity<Response<Stanza>> visualizzaStanza(@PathVariable Long Id,
                                                             HttpServletRequest request) {
      try{
           if (!validationToken.isTokenValid(request)) {
               throw new RuntimeException403("Token non valido");
           }
           Stanza s = stanzaService.visualizzaStanza(Id);
           if(s!=null){
               return ResponseEntity.ok(new Response<>
                       (s, "operazione effettuata con successo"));
           }else{
               throw new ClientRuntimeException("stanza non trovata, id non valido");
           }
      } catch (ClientRuntimeException ce) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                  .body(new Response<>(null, "Errore durante la richiesta: " + ce.getMessage()));

      }catch (RuntimeException403 re) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN)
                  .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));

      }
    }

    @GetMapping(value = "/visualizzaScenari")
    public ResponseEntity<Response<List<Scenario>>> visualizzaScenari(HttpServletRequest request) {
        List<Scenario> scenari;
        try {

            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            scenari = stanzaService.getAllScenari();
            if (scenari == null) {
                return ResponseEntity.status(500)
                        .body(new Response<>(null, "nessuno scenario creato"));
            } else {
                return ResponseEntity
                        .ok(new Response<>(scenari, "operazione effettuata con successo"));
            }
        } catch (RuntimeException403 se) {
            return ResponseEntity.status(403)
                    .body(new Response<>(null, "Errore durante l'operazione: "+se.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, "Errore durante l'operazione"));
        }
    }
}
