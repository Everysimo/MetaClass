package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GestioneUtenzaController {

    @Autowired
    @Qualifier("GestioneUtenzaService")
    private GestioneUtenzaService utenzaService;

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseBoolMessage> login(@RequestBody Utente u, HttpSession session) {
        try {
            if (!utenzaService.loginMeta(u)) {
                throw new RuntimeException("Login non effettuato");
            } else if (session.getAttribute("UserMetaID") == null) {
                session.setAttribute("UserMetaID", u.getMetaId());
                return ResponseEntity.ok(new ResponseBoolMessage(true, "Login effettuato con successo"));
            } else {
                // Utente già loggato
                return ResponseEntity.ok(new ResponseBoolMessage(true, "Utente già loggato"));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseBoolMessage(false, "Errore durante il login: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<ResponseBoolMessage> logout(@RequestBody String MetaId, HttpSession session){
        try {
            if (session.getAttribute("UserMetaID") != null) {
                session.removeAttribute("UserMetaID");
                return ResponseEntity.ok(new ResponseBoolMessage(true, "Logout effettuato con successo"));
            } else {
                return ResponseEntity.ok(new ResponseBoolMessage(true, "Utente non loggato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ResponseBoolMessage(false, "Errore durante il logout"));
        }
    }

    @PostMapping(value = "/modifyUserData/{Id}")
    public ResponseEntity<ResponseBoolMessage> modifyUserData(
            @PathVariable Long Id,
            @RequestBody Map<String, Object> dataMap,
            HttpSession session) {

            Utente u = null;
            ResponseBoolMessage response;
            ResponseEntity<ResponseBoolMessage> responseHTTP;

            response = utenzaService.modificaDatiUtente(Id, dataMap, u);
            if(response.isSuccesso()) {
                 if(u!=null){
                    // Converti l'oggetto utente in formato JSON
                    String userJson = new Gson().toJson(u);
                    System.out.println(userJson);
                    session.setAttribute("UserModified", userJson);
                 }
                 responseHTTP = ResponseEntity.ok(response);
            }else{
                responseHTTP = ResponseEntity.status(500).body(response);
            }
            return responseHTTP;

    }
}
