package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.DataNotFoundException;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;

import java.util.List;


public interface GestioneUtenzaService{
    boolean loginMeta(Utente u);
    Response<Boolean> modificaDatiUtente(String MetaID, Utente u);
    List<Stanza> getStanzeByUserId(String MetaId);
    Utente getUtenteByUserId(String sessionID) throws DataNotFoundException;
    boolean logoutMeta(String token, ValidationToken validationToken);

}
