package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.utility.multipleid.UtenteInMeetingID;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GestioneMeetingController {

    @Autowired
    @Qualifier("GestioneMeetingService")
    private GestioneMeetingService meetingService;

    @Autowired
    private ValidationToken validationToken;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /*@PostMapping(value = "/meetingStatus/{id}")
    public ResponseEntity<Response<Boolean>> meetingStatus(@RequestBody String action, @PathVariable("id") Long idMeeting)
    {
        boolean value;
        String message;

        Meeting meeting = meetingService.findMeetingById(idMeeting);

        if(meeting == null)
        {
            message = "Meeting non trovato";
            return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,message);
        }

        switch (action)
        {
            case "start":
                message = "Meeting avviato";
                value = true;
                //inserire la data di inizio meeting
                break;
            case "stop":
                message = "Meeting terminato";
                value = true;
                //inserire la data di fine meeting
                break;
            default:
                message = "Azione non trovata";
                value = false;
        }
        if(value) return ResponseUtils.getResponseOk(message);
        return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,message);

    }*/

    @PostMapping(value = "/schedulingMeeting")
    public ResponseEntity<Response<Boolean>> schedulingMeeting(@Valid @RequestBody Meeting m, BindingResult result) {
        try {

            //controlla se i parametri passati al meeting sono corretti
            if(result.hasErrors())
            {
                return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,RequestUtils.errorsRequest(result));
            }

            if (!meetingService.creaScheduling(m)) {
                throw new RuntimeException("Meeting non effettuato");
            } else {
                return ResponseEntity.ok(new Response<>(true, "Meeting schedulato con successo"));
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(false, "Errore durante la schedulazione del meeting: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/modificaScheduling")
    public ResponseEntity<Response<Boolean>> modificaScheduling
            (@Valid @RequestBody Meeting m, BindingResult result, HttpServletRequest request) {

        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            //controllo errori di validazione
            if(result.hasErrors())
            {
                return ResponseEntity.status(403)
                        .body(new Response<>(false, RequestUtils.errorsRequest(result)));
            }

            if (!meetingService.modificaScheduling(m)) {
                throw new ServerRuntimeException("modifica non effettuata");
            } else {
                return ResponseEntity.ok(new Response<>(true, "Meeting schedulato con successo"));
            }

        } catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, e.getMessage()));
        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, se.getMessage()));
        }
    }

    @PostMapping(value = "/avviaMeeting/{id_meeting}")
    public ResponseEntity<Response<Boolean>> avviaMeeting (@PathVariable Long id_meeting,
                                                            HttpServletRequest request) {
        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            if(meetingService.avviaMeeting(metaID, id_meeting)){
                return ResponseEntity.ok(new Response<>(true,
                        "Avvio meeting avvenuto con successo"));
            }else{
                throw new ServerRuntimeException("Errore nell'avvio del meeting");
            }


        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, e.getMessage()));
        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, se.getMessage()));
        }sss
    }


    @PostMapping(value = "/accediMeeting/{id_meeting}")
    public ResponseEntity<Response<Boolean>> accediMeeting (@PathVariable Long id_meeting,
                                                            HttpServletRequest request) {
        try {

            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            if(meetingService.accediMeeting(metaID, id_meeting)){
                 return ResponseEntity.ok(new Response<>(true,
                         "Accesso avvenuto con successo"));
            }else{
                throw new ServerRuntimeException("Errore nell'accesso al meeting");
            }
        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, e.getMessage()));
        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, se.getMessage()));
        }

    }
}

