package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
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
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ValidationToken validationToken;


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
    public ResponseEntity<Response<Boolean>> declassaOrganizzatore(@RequestBody RichiestaDTO request, HttpSession session) {
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new Response<>(false, "Utente non loggato"));
            }else{
                return ResponseEntity.ok(stanzaService.downgradeUtente(IdMeta, request.getId_og(), request.getId_stanza()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "/eliminaStanza/{Id}")

    public ResponseEntity<Response<Boolean>> eliminaStanza(@PathVariable Long Id, HttpSession session) {
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new Response<>(false, "Utente non loggato"));
            }else{
                return ResponseEntity.ok(stanzaService.deleteRoom(IdMeta, Id));
            }
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
            @RequestBody Map<String, Object> dataMap,
            HttpSession session) {

        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new Response<>(false, "Utente non loggato"));
            }else {

                Stanza stanza = null;
                Response<Boolean> response;
                ResponseEntity<Response<Boolean>> responseHTTP;
                response = stanzaService.modificaDatiStanza(IdMeta, Id, dataMap, stanza);

                if (response.getValue()) {
                    /*if (stanza != null) {
                        // Converti l'oggetto utente in formato JSON
                        String stanzaJson = new Gson().toJson(stanza);
                        System.out.println(stanzaJson);
                        session.setAttribute("RoomModified", stanzaJson);
                    }*/
                    responseHTTP = ResponseEntity.ok(response);
                } else {
                    responseHTTP = ResponseEntity.status(500).body(response);
                }
                return responseHTTP;

            }

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione"));
        }

    }

    @PostMapping(value = "/promuoviOrganizzatore")
    public ResponseEntity<Response<Boolean>> promuoviOrganizzatore(@RequestBody RichiestaDTO request, HttpSession session) {
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new Response<>(false, "Utente non loggato"));
            }else{
                return ResponseEntity.ok(stanzaService.upgradeUtente(IdMeta, request.getId_og(), request.getId_stanza()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "/accessoStanza")
    public ResponseEntity<AccessResponse<Boolean>> richiestaAccessoStanza(@RequestBody String requestBody, HttpSession session)
    {
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new AccessResponse<>(false, "Utente non loggato", false));
            }else {

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(requestBody);
                String codiceStanza = jsonNode.get("codice").asText();

                return ResponseEntity.ok(stanzaService.accessoStanza(codiceStanza, (String) session.getAttribute("UserMetaID")).getBody());
            }
        } catch (RuntimeException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AccessResponse<>(false, "Errore durante la richiesta: " + e.getMessage(), false));
        }
    }

    @PostMapping(value = "/visualizzaStanza/{Id}")
    public List<Utente> richiestaAccessoStanza(@PathVariable Long Id, HttpSession session)
    {
        return stanzaService.visualizzaStanza(Id);
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
