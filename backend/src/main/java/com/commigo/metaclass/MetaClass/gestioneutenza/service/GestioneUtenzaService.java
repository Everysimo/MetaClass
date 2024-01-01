package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;

import java.util.List;
import java.util.Map;


public interface GestioneUtenzaService{
    boolean loginMeta(Utente u);
    ResponseBoolMessage modificaDatiUtente(Long Id, Map<String, Object> dataMap, Utente u);
    List<Stanza> getStanzeByUserId(String MetaId);

    ResponseBoolMessage upgradeUtente(String id_Uogm, long id_Uog, long id_stanza);
}
