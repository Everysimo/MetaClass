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
     * metodo che consente ad un utente di effettuare il login/registrazione
     * @param u Utente che esegue il login/registrazione
     * @return valore boolean che identifica l'esito dell'operazione
     * @throws ServerRuntimeException
     */
    boolean loginMeta(Utente u) throws ServerRuntimeException;

    /**
     * meotodo che consente la modifica dei dati personali di un determianto utente
     * @param MetaID metaID dell'utente che intende modificare i suoi dati
     * @param params nuovi dati dell'utente
     * @return valore boolean che identifica l'esito dell'operazione
     * @throws RuntimeException403
     */
    boolean modificaDatiUtente(String MetaID, Map<String, Object> params)throws RuntimeException403;

    /**
     * metodo che ritorna tutte le stanze di un utente
     * @param MetaId metaID dell'utente
     * @return lista di stanze di un determinato utente
     * @throws ServerRuntimeException
     */
    List<Stanza> getStanzeByUserId(String MetaId) throws ServerRuntimeException;

    /**
     * ??
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
