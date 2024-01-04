package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import com.commigo.metaclass.MetaClass.utility.response.Response;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
public class GestioneUtenzaController {

    @Autowired
    @Qualifier("GestioneUtenzaService")
    private GestioneUtenzaService utenzaService;

    @PostMapping(value = "/login")
    @CrossOrigin
    public ResponseEntity<Response<Boolean>> login(@RequestBody Utente u, HttpSession session) {
        try {
            if (!utenzaService.loginMeta(u))
                return ResponseUtils.getResponseError("Login non effettuato");

            if (session.getAttribute("UserMetaID") != null)
                return ResponseUtils.getResponseOk("Utente già loggato");

            session.setAttribute("UserMetaID", u.getMetaId());
            return ResponseUtils.getResponseOk("Login effettuato con successo");
        } catch (RuntimeException e) {
            return ResponseUtils.getResponseError("Errore durante il login: " + e.getMessage());
        }
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<Response<Boolean>> logout(HttpSession session) {
        try {
            Enumeration<String> attributes = session.getAttributeNames();
            while (attributes.hasMoreElements()) {
                String attribute = (String) attributes.nextElement();
                System.out.println(attribute+" : "+session.getAttribute(attribute));
            }
            if (session.getAttribute("UserMetaID") != null) {
                session.removeAttribute("UserMetaID");
                return ResponseEntity.ok(new Response<Boolean>(true, "Logout effettuato con successo"));
            } else {
                return ResponseEntity.status(401).body(new Response<Boolean>(false, "Utente non loggato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<Boolean>(false, "Errore durante il logout"));
        }
    }

    @PostMapping(value = "/modifyUserData")
    public ResponseEntity<Response<Boolean>> modifyUserData(
            @RequestBody Utente u,
            HttpSession session) {

        Response<Boolean> response;
        ResponseEntity<Response<Boolean>> responseHTTP;
        String sessionID;

        if((sessionID = (String) session.getAttribute("UserMetaID"))==null){
            return ResponseEntity.status(403)
                    .body(new Response<Boolean>(false,
                            "utente non loggato"));
        }

        response = utenzaService.modificaDatiUtente(sessionID, u);
        if (response.getSuccesso()) {
            if (u != null) {
                // Converti l'oggetto utente in formato JSON
                String userJson = new Gson().toJson(u);
                System.out.println(userJson);
                session.setAttribute("UserModified", userJson);
            }
            responseHTTP = ResponseEntity.ok(response);
        } else {
            responseHTTP = ResponseEntity.status(500).body(response);
        }
        return responseHTTP;

    }

    @CrossOrigin
    @GetMapping(value = "/visualizzaStanze")
    public ResponseEntity<Response<List<Stanza>>> visualizzaStanze(HttpSession session) {
        List<Stanza> stanze;
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");

            if (IdMeta == null)
                return ResponseEntity.status(403).body(new Response<List<Stanza>>(null, "Utente non loggato"));
            stanze = utenzaService.getStanzeByUserId(IdMeta);
            if (stanze == null) {
                return ResponseEntity.status(500)
                        .body(new Response<List<Stanza>>(null,
                                "Errore la ricerca delle stanze"));
            } else if (stanze.isEmpty()) {
                return ResponseEntity
                        .ok(new Response<List<Stanza>>(stanze,
                                "non hai acceduto a nessuna stanza"));
            } else {
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


    @GetMapping(value = "/userDetails")
    public ResponseEntity<Response<Utente>> visualizzaDatiUtente(HttpSession session) {
        Utente utente = null;
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");

            if (IdMeta == null)
                return ResponseEntity.status(403).body(new Response<Utente>(null, "Utente non loggato"));
            utente = utenzaService.getUtenteByUserId(IdMeta);
            if (utente == null) {
                return ResponseEntity.status(500)
                        .body(new Response<Utente>(null,
                                "Errore la ricerca dell'utente"));
            } else {
                return ResponseEntity
                        .ok(new Response<Utente>(utente,
                                "operazione effettuata con successo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new Response<Utente>(null,
                            "Errore durante l'operazione"));
        }
    }

}
