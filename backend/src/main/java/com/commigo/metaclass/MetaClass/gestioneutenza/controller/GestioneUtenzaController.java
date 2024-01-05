package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import com.commigo.metaclass.MetaClass.utility.response.types.LoginResponse;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GestioneUtenzaController {

    @Autowired
    @Qualifier("GestioneUtenzaService")
    private GestioneUtenzaService utenzaService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ValidationToken validationToken;

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse<Boolean>> login(@RequestBody Utente u, HttpServletResponse response) {

        try {

            // Generazione del token JWT usando metaId come identificatore
            String token = jwtTokenUtil.generateToken(u.getMetaId());
            u.setTokenAuth(token);

            // Aggiungi il token al cookie
            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setPath("/");
            response.addCookie(cookie);

            if (!utenzaService.loginMeta(u)){
                throw new RuntimeException("errore nel login");
            }

            return ResponseEntity.ok(new LoginResponse<>(true, "Login effettuato con successo",token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new LoginResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/Manuallogout")
    public ResponseEntity<Response<Boolean>> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            // Invalida il token lato client rimuovendo il cookie
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        cookie.setMaxAge(0); // Imposta la durata del cookie a 0 secondi per invalidarlo
                        response.addCookie(cookie);
                    }
                }
            }

            // Ottieni l'header Authorization dalla richiesta
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;

            // Verifica che l'header Authorization sia presente e inizia con "Bearer "
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // Estrai solo la parte del token dopo "Bearer "
                token = authorizationHeader.substring(7);
            }else{
                throw new RuntimeException403("Token non valido");
            }

            // Rimuovi il token nel sistema
            if (utenzaService.logoutMeta(token, validationToken)) {
                return ResponseEntity.ok(new Response<Boolean>(true, "Utente disconnesso con successo"));
            } else {
                throw new ServerRuntimeException("Errore nella rimozione del token dell'utente");
            }
        } catch (RuntimeException403 e) {
            return ResponseEntity.status(403).body(new Response<>(false, e.getMessage()));
        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500).body(new Response<>(false, se.getMessage()));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(new Response<>(false, "errore"));
        }
    }

    @PostMapping(value = "/modifyUserData")
    public ResponseEntity<Response<Boolean>> modifyUserData(@RequestBody Utente u,
            HttpSession session) {

        Response<Boolean> response;
        ResponseEntity<Response<Boolean>> responseHTTP;
        String sessionID;

        if((sessionID = (String) session.getAttribute("UserMetaID"))==null){
            return ResponseEntity.status(403)
                    .body(new Response<>(false, "utente non loggato"));
        }

        response = utenzaService.modificaDatiUtente(sessionID, u);
        if (response.getValue()) {
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
                return ResponseEntity.status(403).body(new Response<>(null, "Utente non loggato"));
            stanze = utenzaService.getStanzeByUserId(IdMeta);
            if (stanze == null) {
                return ResponseEntity.status(500)
                        .body(new Response<>(null, "Errore la ricerca delle stanze"));
            } else if (stanze.isEmpty()) {
                return ResponseEntity
                        .ok(new Response<>(stanze, "Non hai accesso a nessuna stanza"));
            } else {
                return ResponseEntity
                        .ok(new Response<>(stanze, "operazione effettuata con successo"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, "Errore durante l'operazione"));
        }
    }


    @GetMapping(value = "/userDetails")
    public ResponseEntity<Response<Utente>> visualizzaDatiUtente(HttpSession session) {
        Utente utente;
        try {
            String IdMeta = (String) session.getAttribute("UserMetaID");

            if (IdMeta == null)
                return ResponseEntity.status(403).body(new Response<>(null, "Utente non loggato"));
            utente = utenzaService.getUtenteByUserId(IdMeta);
            if (utente == null) {
                return ResponseEntity.status(500)
                        .body(new Response<>(null,"Errore la ricerca dell'utente"));
            } else {
                return ResponseEntity.ok(new Response<>(utente, "operazione effettuata con successo"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, "Errore durante l'operazione"));
        }
    }

}
