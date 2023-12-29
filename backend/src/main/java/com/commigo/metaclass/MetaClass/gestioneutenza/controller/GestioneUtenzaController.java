package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneutenza.service.GestioneUtenzaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GestioneUtenzaController {

    @Autowired
    @Qualifier("UserService")
    private GestioneUtenzaService utenzaService;

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public boolean login(@RequestBody Utente u, HttpSession session){
        try {
            if (!utenzaService.loginMeta(u)) {
                   throw new RuntimeException("login non effettuato");
            }
            if(session.getAttribute("userID")==null) {
                session.setAttribute("UserID", u.getId());
            }
            return true;
        }catch(RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }
}
