package com.commigo.metaclass.MetaClass.gestionestanza.controller;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException401;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.MetaClass.utility.MapValidator;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.AccessResponse;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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


    /**
     * metodo che gestisce la richiesta di ban di un utente all'interno di una stanza
     *
     * @param IdStanza
     * @param IdUtente
     * @param request
     * @return
     *
     */
    @PostMapping(value = "/banUtente/{IdStanza}/{IdUtente}")
    public ResponseEntity<Response<Boolean>> banUtente(@PathVariable Long IdStanza,
                                                              @PathVariable Long IdUtente,
                                                              HttpServletRequest request) {

        try {
            //controllo del token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());
            return stanzaService.banUtente(IdStanza,metaID,IdUtente);

        } catch (RuntimeException403 re) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, "Errore durante l'operazione: " + re.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione"));
        }
    }

    /**
     *
     * @param s
     * @param result
     * @param request
     * @return
     */
    @PostMapping(value = "/creastanza")
    public ResponseEntity<Response<Boolean>> creaStanza(@Valid @RequestBody Stanza s,
                                                        BindingResult result,
                                                        HttpServletRequest request){
        try
        {
            //validazione dl token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            if(result.hasErrors()) {
                throw new RuntimeException403(RequestUtils.errorsRequest(result));
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            if(!stanzaService.creaStanza(s,metaID)){
                throw new ServerRuntimeException("errore nel salvataggio dell stanza");
            }
            return ResponseUtils.getResponseOk("Corretto");


        }catch (ServerRuntimeException e) {
            return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,"Errore durante la richiesta: " + e.getMessage());
        }catch(RuntimeException403 se){
            return ResponseUtils.getResponseError(HttpStatus.valueOf(403),
                    "Errore durante la richiesta: " + se.getMessage());
        }catch(Exception ge){
            return ResponseUtils.getResponseError(HttpStatus.valueOf(500),
                    "Errore durante la richiesta: " + ge.getMessage());
        }
    }

    /**
     *
     * @param IdStanza
     * @param IdUtente
     * @param request
     * @return
     */
    @PostMapping(value = "/declassaOrganizzatore/{IdStanza}/{IdUtente}")
    public ResponseEntity<Response<Boolean>> declassaOrganizzatore(@PathVariable Long IdStanza,
                                                                   @PathVariable Long IdUtente,
                                                                   HttpServletRequest request) {
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            return ResponseEntity.ok(stanzaService.downgradeUtente(metaID, IdUtente, IdStanza));

        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione: "+se.getMessage()));
        } catch (RuntimeException403 re) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, "Errore durante l'operazione: "+re.getMessage()));
        }
    }

    /**
     *
     * @param Id
     * @param request
     * @return
     */
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

    /**
     *
     * @param idStanza
     * @param idUtente
     * @param scelta
     * @param request
     * @return
     */
    @PostMapping(value = "/gestioneAccessi/{idStanza}/{idUtente}")
    public ResponseEntity<Response<Boolean>> gestioneAccessi(@PathVariable Long idStanza,
                                                             @PathVariable Long idUtente,
                                                             @RequestBody String scelta,
                                                             HttpServletRequest request){
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(scelta);
            Boolean Newscelta = jsonNode.get("scelta").asBoolean();

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());
            return stanzaService.gestioneAccesso(metaID, idUtente, idStanza, Newscelta);

        }catch(RuntimeException403 re){
            return ResponseEntity.status(403)
                    .body(new Response<>(false, "Errore nell'operazione: "+re.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione" + e.getMessage()));
        }
    }


    /**
     *
     * @param Id
     * @param params
     * @param request
     * @return
     */
    @PostMapping(value = "/modifyRoomData/{Id}")
    public ResponseEntity<Response<Boolean>> modifyRoomData(@PathVariable Long Id,
                                                            @RequestBody Map<String,Object> params,
                                                            HttpServletRequest request) {

        try {
                //controllo del token
                if (!validationToken.isTokenValid(request)) {
                    throw new RuntimeException403("Token non valido");
                }

                //validazione della map
                MapValidator.stanzaValidate(params);

                if(!stanzaService.modificaDatiStanza(params,Id)){
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
        }catch(ClientRuntimeException ce) {
            return ResponseEntity.status(400)
                    .body(new Response<>(false, ce.getMessage()));
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione"));
        }
    }

    /**
     *
     * @param IdStanza
     * @param IdUtente
     * @param request
     * @return
     */
    @PostMapping(value = "/promuoviOrganizzatore/{IdStanza}/{IdUtente}")
    public ResponseEntity<Response<Boolean>> promuoviOrganizzatore(@PathVariable Long IdStanza,
                                                                   @PathVariable Long IdUtente,
                                                                   HttpServletRequest request) {
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            return ResponseEntity.ok(stanzaService.upgradeUtente(metaID, IdUtente, IdStanza));

        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, "Errore durante l'operazione: "+se.getMessage()));
        } catch (RuntimeException403 re) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, "Errore durante l'operazione: "+re.getMessage()));
        }
    }

    /**
     *
     * @param requestBody
     * @param request
     * @return
     */
    @PostMapping(value = "/accessoStanza")
    public ResponseEntity<AccessResponse<Boolean>> richiestaAccessoStanza(@RequestBody String requestBody,
                                                                          HttpServletRequest request) {
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            JsonNode codiceNode = jsonNode.get("codice");

            //controllo se il codice è null
            if (codiceNode == null)
                throw new RuntimeException403("l'attributo deve essere nominato 'codice' e non diversamente");

            //controllo se l'attributo è testuale
            if(!codiceNode.isTextual())
                throw new RuntimeException403("l'attributo deve essere una stringa");

            String codiceStanza = codiceNode.asText();

            return ResponseEntity.ok(stanzaService.accessoStanza(codiceStanza, metaID).getBody());

        }catch (JsonProcessingException je) {
            return ResponseEntity.status(403)
                    .body(new AccessResponse<>(false, "Errore durante la richiesta: il body della tua richiesta è vuoto", false));
        } catch (RuntimeException403 re) {
            return ResponseEntity.status(403)
                    .body(new AccessResponse<>(false, "Errore durante la richiesta: "+re.getMessage(), false));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AccessResponse<>(false, "Errore durante la richiesta: " + e.getMessage(), false));
        }
    }

    /**
     *
     * @param Id
     * @param request
     * @return
     * @throws RuntimeException403
     */
    public ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(@PathVariable Long Id, HttpServletRequest request) throws RuntimeException403 {
        try{
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            return stanzaService.visualizzaUtentiBannatiInStanza(Id);

        }catch (RuntimeException403 re) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
        }
    }

    /**
     *
     * @param Id
     * @param request
     * @return
     * @throws RuntimeException403
     */
    @PostMapping(value = "/visualizzaUtentiInStanza/{Id}")
    public ResponseEntity<Response<List<Utente>>> visualizzaUtentiInStanza(@PathVariable Long Id, HttpServletRequest request) throws RuntimeException403 {
        try{
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            return stanzaService.visualizzaUtentiInStanza(Id);

        }catch (RuntimeException403 re) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
        }
    }


    /**
     *
     * @param Id
     * @param request
     * @return
     * @throws RuntimeException403
     */
    @PostMapping(value = "/visualizzaUtentiInAttesaInStanza/{Id}")
    ResponseEntity<Response<List<Utente>>> visualizzaUtentiInAttesaInStanza(@PathVariable Long Id, HttpServletRequest request) throws RuntimeException403 {
        try{
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());
            return stanzaService.visualizzaUtentiInAttesaInStanza(Id, metaID);

        }catch (RuntimeException403 re) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
        }
    }

    /**
     *
     * @param Id
     * @param request
     * @return
     */
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

    /**
     *
     * @param request
     * @return
     */
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

    /**
     *
     * @param Id
     * @param request
     * @return
     */
    @PostMapping(value = "/visualizzaScenarioStanza/{Id}")
    public ResponseEntity<Response<Scenario>> visualizzaScenarioStanza(@PathVariable Long Id,
                                                                       HttpServletRequest request) {
        try {

            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            return  stanzaService.findStanza(Id);

        } catch (RuntimeException403 se) {
            return ResponseEntity.status(403)
                    .body(new Response<>(null, "Errore durante l'operazione: " + se.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, "Errore durante l'operazione"));
        }

    }

    /**
     *
     * @param Id_stanza
     * @param Id_scenario
     * @param request
     * @return
     */
    @PostMapping(value = "/modificaScenario/{Id_stanza}/{Id_scenario}")
    public ResponseEntity<Response<Boolean>> modificaScenario(@PathVariable Long Id_stanza,
                                                              @PathVariable Long Id_scenario,
                                                              HttpServletRequest request){
        try{

            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());
            return stanzaService.modificaScenario(metaID, Id_scenario, Id_stanza);

        } catch (RuntimeException403 e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
        }
    }

    /**
     *
     * @param IdStanza
     * @param IdUtente
     * @param nome
     * @param request
     * @return
     */
    @PostMapping(value = "/modificaNomePartecipante/{IdStanza}/{IdUtente}")
    public ResponseEntity<Response<Boolean>> modificaNomePartecipante(@PathVariable Long IdStanza,
                                                                      @PathVariable Long IdUtente,
                                                                      @RequestBody String nome,
                                                                      HttpServletRequest request) {

        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(nome);
            String NuovoNome = jsonNode.get("nome").asText();

            return stanzaService.modificaNomePartecipante(metaID, IdStanza, IdUtente, NuovoNome);

        } catch (RuntimeException403 e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
        }
    }

    /**
     *
     * @param IdStanza
     * @param IdUtente
     * @param request
     * @return
     */
    @PostMapping(value = "/kickarePartecipante/{IdStanza}/{IdUtente}")
    public ResponseEntity<Response<Boolean>> kickPartecipante(@PathVariable Long IdStanza,
                                                                      @PathVariable Long IdUtente,
                                                                      HttpServletRequest request) {

        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            return stanzaService.kickPartecipante(metaID, IdStanza, IdUtente);

        } catch (RuntimeException403 e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
        }
    }

    /**
     *
     * @param id_stanza
     * @param request
     * @return
     */
    @PostMapping(value = "/getRuolo/{id_stanza}")
    public ResponseEntity<Response<Ruolo>> getRuoloByUserAndByStanza(@PathVariable Long id_stanza,
                                                              HttpServletRequest request){
        try{

            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            Ruolo r = stanzaService.getRuoloByUserAndStanzaID(metaID,id_stanza);
            if(r == null)  throw new ServerRuntimeException("errore nel recapito del ruolo");
            else  return ResponseEntity.ok(new Response<>(r,"ruolo recapitato con successo"));

        } catch (ServerRuntimeException | RuntimeException403 e) {

        // Gestisci le eccezioni e restituisci una risposta appropriata
        int statusCode = (e instanceof ServerRuntimeException) ? 500 : 403;
        return ResponseEntity.status(statusCode)
                .body(new Response<>(null, "Errore durante l'operazione: " + e.getMessage()));
       }
    }

    /**
     *
     * @param IdStanza
     * @param IdUtente
     * @param request
     * @return
     */
    @PostMapping(value = "/silenziarePartecipante/{IdStanza}/{IdUtente}")
    public ResponseEntity<Response<Boolean>> SilenziaPartecipante(@PathVariable Long IdStanza,
                                                                @PathVariable Long IdUtente,
                                                                HttpServletRequest request) {

        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            return stanzaService.SilenziaPartecipante(metaID, IdStanza, IdUtente);

        } catch (RuntimeException403 e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "/unmute/{IdStanza}")
    public ResponseEntity<Response<Boolean>> unmute(@PathVariable Long IdStanza,
                                                    HttpServletRequest request) {

        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            return stanzaService.Unmute(metaID, IdStanza);

        } catch (RuntimeException403 e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
        }
    }

    @PostMapping(value = "/mute/{IdStanza}")
    public ResponseEntity<Response<Boolean>> mute(@PathVariable Long IdStanza,
                                                    HttpServletRequest request) {

        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            return stanzaService.mute(metaID, IdStanza);

        } catch (RuntimeException403 e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body(new Response<>(null, "Errore nell'operazione"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<>(null, "Errore durante l'operazione"));
        }
    }
}
