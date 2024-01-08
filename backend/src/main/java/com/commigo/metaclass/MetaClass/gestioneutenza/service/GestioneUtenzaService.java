package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.DataNotFoundException;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException401;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;

import java.util.List;
import java.util.Map;


public interface GestioneUtenzaService{
    boolean loginMeta(Utente u) throws ServerRuntimeException;
    boolean modificaDatiUtente(String MetaID, Map<String, Object> params)throws RuntimeException403;
    List<Stanza> getStanzeByUserId(String MetaId) throws ServerRuntimeException;
    Utente getUtenteByUserId(String sessionID) throws DataNotFoundException;
    boolean logoutMeta(String token, ValidationToken validationToken) throws ServerRuntimeException;

}
