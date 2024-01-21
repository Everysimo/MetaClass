package com.commigo.metaclass.gestionestimaduratameeting.adapter;

import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.ServerRuntimeException;

import java.time.Duration;

public interface StimaDurataMeetingAdapter {

  public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel)
      throws ServerRuntimeException;
}
