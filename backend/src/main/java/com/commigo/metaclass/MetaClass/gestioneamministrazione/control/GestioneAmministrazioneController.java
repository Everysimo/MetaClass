package com.commigo.metaclass.MetaClass.gestioneamministrazione.control;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.service.GestioneAmministrazioneService;
import com.commigo.metaclass.MetaClass.utility.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GestioneAmministrazioneController {

    @Autowired
    @Qualifier("GestioneAmministrazioneService")
    private GestioneAmministrazioneService gestioneamministrazione;

    @PostMapping(value = "admin/updateCategoria")
    public ResponseEntity<Response<Boolean>> updateCategoria(@RequestBody Categoria c, HttpSession session) {
        try {
            if (!gestioneamministrazione.updateCategoria(c)) {
                throw new Exception("Errore durante l'inserimento della categoria");
            } else {
                return ResponseEntity.ok(new Response<Boolean>(true,
                        "categoria creata con successo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<Boolean>(false, e.getMessage()));
        }
    }

    @PostMapping(value = "admin/updateScenario")
    public ResponseEntity<Response<Boolean>> updateCategoria(@RequestBody String requestBody, HttpSession session)
            throws JsonProcessingException {

        // Convertita la stringa JSON in un oggetto Scenario usando ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        Scenario s = objectMapper.readValue(requestBody, Scenario.class);

        // Deserializzazione della stringa JSON in un albero di nodi JsonNode
        JsonNode jsonNode = objectMapper.readTree(requestBody);

        // Estratto il valore di "id_categoria" come un Long
        long idCategoria = jsonNode.get("id_categoria").asLong();

        try {
            if (!gestioneamministrazione.updateScenario(s, idCategoria)) {
                throw new Exception("Errore durante l'inserimento dello scenario");
            } else {
                return ResponseEntity.ok(new Response<Boolean>(true,
                        "scenario creato con successo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<Boolean>(false, e.getMessage()));
        }
    }

    @GetMapping(value = "admin/allStanze")
    public ResponseEntity<Response<List<Stanza>>> visualizzaStanze() {
        List<Stanza> stanze;
        try {
            stanze = gestioneamministrazione.getStanze();
            if(stanze == null){
                return ResponseEntity.status(500)
                        .body(new Response<List<Stanza>>(null,
                                "Errore la ricerca delle stanze"));
            }else if(stanze.isEmpty()){
                return ResponseEntity
                        .ok(new Response<List<Stanza>>(stanze,
                                "nessuna stanza creata"));
            }else{
                return ResponseEntity
                        .ok(new Response<List<Stanza>>(stanze,
                                "operazione effettuata con successo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new Response<List<Stanza>>(null,
                            "Errore durante l'operazione"));
        }
    }
}