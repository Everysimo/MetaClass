package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;
import com.commigo.metaclass.MetaClass.gestioneutenza.exception.DataNotFoundException;
import com.commigo.metaclass.MetaClass.utility.response.Response;

import java.util.List;
import java.util.Map;


public interface GestioneUtenzaService{
    boolean loginMeta(Utente u);
    Response<Boolean> modificaDatiUtente(String sessionID, Utente u);
    List<Stanza> getStanzeByUserId(String MetaId);
    Utente getUtenteByUserId(String sessionID) throws DataNotFoundException;

}
