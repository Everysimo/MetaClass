package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;

import java.util.Map;


public interface GestioneUtenzaService{
    boolean loginMeta(Utente u);
    ResponseBoolMessage modificaDatiUtente(Long Id, Map<String, Object> dataMap, Utente u);
}
