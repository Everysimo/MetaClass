package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import com.commigo.metaclass.MetaClass.utility.response.Response;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GestioneStanzaControl {

    @Autowired
    @Qualifier("GestioneStanzaService")
    private GestioneStanzaService stanzaService;

    //MICHELE: So che funziona, ma valuta anche @RequestBody Stanza s
    @PostMapping(value = "/creastanza")
    public ResponseEntity<Response<Boolean>> creaStanza(@RequestBody String requestBody, HttpSession session)
    {
        try
        {
            JsonNode jsonNode = new ObjectMapper().readTree(requestBody);
            Response<Boolean> response = new Response<>();
            Stanza stanza = new Stanza();

            String nome,codiceStanza,descrizione, control_str;
            boolean tipoAccesso;
            int maxPosti;

            nome = jsonNode.get("nome").asText();

            if(nome.length() <= 1)
                return ResponseUtils.getResponseError("Lunghezza nome errata");

            if(!nome.matches("^[A-Z][a-zA-Z0-9]*$"))
                return ResponseUtils.getResponseError("Formato nome errato");

            codiceStanza = jsonNode.get("codiceStanza").asText();

            if(codiceStanza.length() != 6)
                return ResponseUtils.getResponseError(("Lunghezza codice_stanza errato"));

            if(!codiceStanza.matches("^[0-9]{6}$"))
                return ResponseUtils.getResponseError("Formato codice_stanza errato");

            descrizione = jsonNode.get("descrizione").asText();

            if(descrizione.isEmpty() || descrizione.length() > 254)
                return ResponseUtils.getResponseError("Lunghezza descrizione errata");

            if(descrizione.matches("^[A-Z][a-zA-Z0-9.,!?()'\"\\-\\s]*$"))
                return ResponseUtils.getResponseError("Formato descrizione errata");

            control_str = jsonNode.get("tipoAccesso").asText();

            if(!control_str.matches("^(true|false)$"))
                return ResponseUtils.getResponseError("Formato tipo_accesso errato");

            tipoAccesso = Boolean.parseBoolean(control_str);

            control_str = jsonNode.get("maxPosti").asText();

            if(!control_str.matches("^(?!0$)[0-9]{1,3}$"))
                return ResponseUtils.getResponseError("Formato max_posti non errato");

            maxPosti = Integer.parseInt(control_str);

            stanzaService.creaStanza(nome,codiceStanza,descrizione,tipoAccesso,maxPosti);
            return ResponseUtils.getResponseOk("Corretto");
        }
        catch (RuntimeException | JsonProcessingException e)
        {
            return ResponseUtils.getResponseError("Errore durante la richiesta: " + e.getMessage());
        }
    }

    @PostMapping(value = "/accessoStanza")
    public ResponseEntity<ResponseBoolMessage> richiestaAccessoStanza(@RequestBody String requestBody, HttpSession session)
    {
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new ResponseBoolMessage(false, "Utente non loggato"));
            }else {

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(requestBody);
                String codiceStanza = jsonNode.get("codice").asText();

                return ResponseEntity.ok(stanzaService.accessoStanza(codiceStanza, (String) session.getAttribute("UserMetaID")).getBody());
            }
        } catch (RuntimeException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseBoolMessage(false, "Errore durante la richiesta: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/promuoviOrganizzatore")
    public ResponseEntity<Response<Boolean>> promuoviOrganizzatore(@RequestBody RichiestaDTO request, HttpSession session) {
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new Response<Boolean>(false, "Utente non loggato"));
            }else{
                return ResponseEntity.ok(stanzaService.upgradeUtente(IdMeta, request.getId_og(), request.getId_stanza()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new Response<Boolean>(false,
                            "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "/declassaOrganizzatore")
    public ResponseEntity<Response<Boolean>> declassaOrganizzatore(@RequestBody RichiestaDTO request, HttpSession session) {
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new Response<Boolean>(false, "Utente non loggato"));
            }else{
                return ResponseEntity.ok(stanzaService.downgradeUtente(IdMeta, request.getId_og(), request.getId_stanza()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new Response<Boolean>(false,
                            "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "/eliminaStanza/{Id}")

    public ResponseEntity<Response<Boolean>> eliminaStanza(@PathVariable Long Id, HttpSession session) {
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");
            if (IdMeta == null) {
                return ResponseEntity.status(403).body(new Response<Boolean>(false, "Utente non loggato"));
            }else{
                return ResponseEntity.ok(stanzaService.deleteRoom(IdMeta, Id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new Response<Boolean>(false,
                            "Errore durante l'operazione"));
        }
    }
}
