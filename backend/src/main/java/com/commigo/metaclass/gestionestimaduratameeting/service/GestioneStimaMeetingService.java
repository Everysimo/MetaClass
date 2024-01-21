package com.commigo.metaclass.gestionestimaduratameeting.service;

import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;

import java.time.Duration;

public interface GestioneStimaMeetingService {
  public Double getDurataMeeting(Long id_stanza) throws RuntimeException403, ServerRuntimeException;

  public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel)
      throws ServerRuntimeException;
}
