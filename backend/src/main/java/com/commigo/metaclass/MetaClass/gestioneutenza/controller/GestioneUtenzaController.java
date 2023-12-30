package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GestioneUtenzaController {

    @Autowired
    @Qualifier("GestioneUtenzaService")
    private GestioneUtenzaService utenzaService;

    @PostMapping(value = "/login")
    public ResponseEntity<RispostaLoginAndLogout> login(@RequestBody Utente u, HttpSession session) {
        try {
            if (!utenzaService.loginMeta(u)) {
                throw new RuntimeException("Login non effettuato");
            } else if (session.getAttribute("UserMetaID") == null) {
                session.setAttribute("UserMetaID", u.getMetaId());
                return ResponseEntity.ok(new RispostaLoginAndLogout(true, "Login effettuato con successo"));
            } else {
                // Utente già loggato
                return ResponseEntity.ok(new RispostaLoginAndLogout(true, "Utente già loggato"));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RispostaLoginAndLogout(false, "Errore durante il login: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<RispostaLoginAndLogout> logout(@RequestBody String MetaId, HttpSession session){
        try {
            if (session.getAttribute("UserMetaID") != null) {
                session.removeAttribute("UserMetaID");
                return ResponseEntity.ok(new RispostaLoginAndLogout(true, "Logout effettuato con successo"));
            } else {
                return ResponseEntity.ok(new RispostaLoginAndLogout(true, "Utente non loggato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new RispostaLoginAndLogout(false, "Errore durante il logout"));
        }
    }
}
