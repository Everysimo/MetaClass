package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import com.commigo.metaclass.MetaClass.utility.MapValidator;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<LoginResponse<Boolean>> login(@RequestBody Utente u,
                                                        HttpServletResponse response,
                                                        BindingResult result) {

        try {

            // Generazione del token JWT usando metaId come identificatore
            String token = jwtTokenUtil.generateToken(u.getMetaId());
            u.setTokenAuth(token);

            //controllo errori di validazione
            if(result.hasErrors())
            {
                throw new RuntimeException403(RequestUtils.errorsRequest(result));

            }

            // Aggiungi il token al cookie
            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setPath("/");
            response.addCookie(cookie);

            if (!utenzaService.loginMeta(u)){
                throw new ServerRuntimeException("errore nel login");
            }

            return ResponseEntity.ok(new LoginResponse<>(true, "Login effettuato con successo",token, u.isAdmin()));
        } catch (ServerRuntimeException e) {
            return ResponseEntity.status(500).body(new LoginResponse<>(false, e.getMessage(), null, false));
        } catch (RuntimeException403 e) {
            return ResponseEntity.status(403).body(new LoginResponse<>(false, e.getMessage(), null, false));
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
            return ResponseEntity.status(500).body(new Response<>(false, "errore"));
        }
    }

    @PostMapping(value = "/modifyUserData")
    public ResponseEntity<Response<Boolean>> modifyUserData(@RequestBody Map<String, Object> params,
                                                            HttpServletRequest request) {
        try{

            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            //Validazione dati utente
            MapValidator.utenteValidate(params);

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            if(!utenzaService.modificaDatiUtente(metaID,params)) {
                throw new ServerRuntimeException("Modifica Utente non effettuata");
            }else{
                return ResponseEntity.ok(new Response<>(true, "Utente modificato con successo"));
            }
        }catch(RuntimeException403 e){
            return ResponseEntity.status(403).body(new Response<>(null, e.getMessage()));
        } catch (ClientRuntimeException e) {
            return ResponseEntity.status(400).body(new Response<>(null, e.getMessage()));
        } catch (ServerRuntimeException e) {
            return ResponseEntity.status(500).body(new Response<>(null, e.getMessage()));
        }
    }

    @CrossOrigin
    @GetMapping(value = "/visualizzaStanze")
    public ResponseEntity<Response<List<Stanza>>> visualizzaStanze(HttpServletRequest request) {
        List<Stanza> stanze;
        try {

            //validazione del token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            stanze = utenzaService.getStanzeByUserId(metaID);
            if (stanze == null) {
                throw new ServerRuntimeException("Errore la ricerca delle stanze");
            } else if (stanze.isEmpty()) {
                return ResponseEntity
                        .ok(new Response<>(stanze, "Non hai accesso a nessuna stanza"));
            } else {
                return ResponseEntity
                        .ok(new Response<>(stanze, "operazione effettuata con successo"));
            }
        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, "Errore durante l'operazione: "+se.getMessage()));
        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(null, "Errore durante l'operazione: "+e.getMessage()));
        }
    }


    @GetMapping(value = "/userDetails")
    public ResponseEntity<Response<Utente>> visualizzaDatiUtente(HttpServletRequest request) {
        Utente utente;
        try {

            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String IdMeta = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());
            utente = utenzaService.getUtenteByUserId(IdMeta);
            if (utente == null) {
                return ResponseEntity.status(500)
                        .body(new Response<>(null,"Errore la ricerca dell'utente"));
            } else {
                return ResponseEntity.ok(new Response<>(utente, "operazione effettuata con successo"));
            }
        }catch (RuntimeException403 e) {
                return ResponseEntity.status(403).body(new Response<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, "Errore durante l'operazione"));
        }
    }

}
