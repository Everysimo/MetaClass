package com.commigo.metaclass.MetaClass.controller;

import com.commigo.metaclass.MetaClass.service.GestioneUtenzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GestioneUtenzaController {

    @Autowired
    @Qualifier("UserService")
    private GestioneUtenzaService utenzaService;

    @RequestMapping("/login")
    public void login(){
         System.out.println("eeee");
    }
}
