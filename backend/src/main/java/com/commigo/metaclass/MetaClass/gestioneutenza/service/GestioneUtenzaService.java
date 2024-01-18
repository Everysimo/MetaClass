package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.DataNotFoundException;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;

import java.util.List;
import java.util.Map;


public interface GestioneUtenzaService{
    /**
     *
     * @param u
     * @return
     * @throws ServerRuntimeException
     */
    boolean loginMeta(Utente u) throws ServerRuntimeException;

    /**
     *
     * @param MetaID
     * @param params
     * @return
     * @throws RuntimeException403
     */
    boolean modificaDatiUtente(String MetaID, Map<String, Object> params)throws RuntimeException403;

    /**
     *
     * @param MetaId
     * @return
     * @throws ServerRuntimeException
     */
    List<Stanza> getStanzeByUserId(String MetaId) throws ServerRuntimeException;

    /**
     *
     * @param sessionID
     * @return
     * @throws DataNotFoundException
     */
    Utente getUtenteByUserId(String sessionID) throws DataNotFoundException;

    /**
     *
     * @param metaID
     * @param validationToken
     * @return
     * @throws ServerRuntimeException
     */
    boolean logoutMeta(String metaID, ValidationToken validationToken) throws ServerRuntimeException;

}
