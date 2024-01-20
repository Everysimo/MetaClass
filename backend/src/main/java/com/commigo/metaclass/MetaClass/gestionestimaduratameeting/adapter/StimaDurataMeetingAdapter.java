package com.commigo.metaclass.MetaClass.gestionestimaduratameeting.adapter;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;

import java.time.Duration;
import java.util.List;

public interface StimaDurataMeetingAdapter {

    public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel) throws ServerRuntimeException;

}



