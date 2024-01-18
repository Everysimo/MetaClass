package com.commigo.metaclass.MetaClass.gestionestimaduratameeting.service;

import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionestimaduratameeting.adapter.StimaDurataMeetingAdapter;
//import com.commigo.metaclass.MetaClass.gestionestimaduratameeting.adapter.StimaDurataMeetingAdapterImpl;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service("GestioneStimaMeetingService")
public class GestioneStimaMeetingServiceImpl implements GestioneStimaMeetingService {

    /**
     * Adapter per ottenere i risultati dell'agente intelligente.
     */
    private StimaDurataMeetingAdapter stimaProgressiAdapter;


    /**
     * constructor
     */
   // public GestioneStimaMeetingServiceImpl(){
       // stimaProgressiAdapter = new StimaDurataMeetingAdapterImpl();
    //}


   /**
    *
    * @param id_stanza
    * @return
    * @throws RuntimeException403
    * @throws ServerRuntimeException
    */
    @Override
    public Double getDurataMeeting(Long id_stanza) throws RuntimeException403, ServerRuntimeException {
        return stimaProgressiAdapter.getDurataMeeting(id_stanza);
    }

    /**
     *
     * @param u
     * @param durata
     * @param immersionLevel
     * @throws ServerRuntimeException
     */
    @Override
    public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel)
            throws ServerRuntimeException {
        stimaProgressiAdapter.addUtenteInDataset(u,durata,immersionLevel);
    }
}
