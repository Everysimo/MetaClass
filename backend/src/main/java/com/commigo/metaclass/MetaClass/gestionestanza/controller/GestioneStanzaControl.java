package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GestioneStanzaControl {

    @Autowired
    @Qualifier("GestioneStanzaService")
    private GestioneStanzaService stanzaService;

    //MICHELE: So che funziona, ma valuta anche @RequestBody Stanza s
    @PostMapping(value = "/creastanza")
    public ResponseEntity<Response<Boolean>> creaStanza(@RequestBody String requestBody, BindingResult result)
    {
        if(result.hasErrors())
        {
            return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR, RequestUtils.errorsRequest(result));
        }
        try
        {
            JsonNode jsonNode = new ObjectMapper().readTree(requestBody);

            String nome = jsonNode.get("nome").asText();
            String codiceStanza = jsonNode.get("codiceStanza").asText();
            String descrizione = jsonNode.get("descrizione").asText();
            boolean tipoAccesso = jsonNode.get("tipoAccesso").asBoolean();
            int maxPosti = jsonNode.get("maxPosti").asInt();

            stanzaService.creaStanza(nome,codiceStanza,descrizione,tipoAccesso,maxPosti);
            return ResponseUtils.getResponseOk("Corretto");
        }
        catch (RuntimeException | JsonProcessingException e)
        {
            return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,"Errore durante la richiesta: " + e.getMessage());
        }
    }

    @PostMapping(value = "/accessoStanza")
    public ResponseEntity<Response<Boolean>> richiestaAccessoStanza(@RequestBody String requestBody, HttpSession session)
    {
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new Response<>(false, "Utente non loggato"));
            }else {

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(requestBody);
                String codiceStanza = jsonNode.get("codice").asText();

                return ResponseEntity.ok(stanzaService.accessoStanza(codiceStanza, (String) session.getAttribute("UserMetaID")).getBody());
            }
        } catch (RuntimeException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(false, "Errore durante la richiesta: " + e.getMessage()));
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
}
