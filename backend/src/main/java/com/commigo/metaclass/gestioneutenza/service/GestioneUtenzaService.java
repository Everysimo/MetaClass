package com.commigo.metaclass.gestioneutenza.service;

import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.DataNotFoundException;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.webconfig.ValidationToken;

import java.util.List;
import java.util.Map;

public interface GestioneUtenzaService {

  boolean loginMeta(Utente u) throws ServerRuntimeException;

  boolean modificaDatiUtente(String metaId, Map<String, Object> params) throws RuntimeException403;

  List<Stanza> getStanzeByUserId(String metaId) throws ServerRuntimeException;

  Utente getUtenteByUserId(String sessionID) throws DataNotFoundException;

  boolean logoutMeta(String metaId, ValidationToken validationToken) throws ServerRuntimeException;
}
