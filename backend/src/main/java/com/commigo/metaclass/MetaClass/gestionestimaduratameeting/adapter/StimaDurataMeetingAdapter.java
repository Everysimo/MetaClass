package com.commigo.metaclass.MetaClass.gestionestimaduratameeting.adapter;

import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;

import java.time.Duration;

public interface StimaDurataMeetingAdapter {

    public Double getDurataMeeting(Long id_stanza) throws RuntimeException403, ServerRuntimeException;
    public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel) throws ServerRuntimeException;

}



