package com.commigo.metaclass.gestionestimaduratameeting.service;

import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import java.time.Duration;

/** Interfaccia che per servizi legati al modulo AI. */
public interface GestioneStimaMeetingService {
  /**
   * Metodo che permette di visualizzare la durata di un meeting.
   *
   * @param idStanza id della stanza
   * @return valore double
   * @throws RuntimeException403 eccezione generata quando avviene un errore Client.
   * @throws ServerRuntimeException eccezione generata quando avviene un errore Server
   */
  public Double getDurataMeeting(Long idStanza) throws RuntimeException403, ServerRuntimeException;

  /**
   * Metodo che richiama l'adapter per arricchire il dataset.
   *
   * @param u Istanza di Utente-
   * @param durata Durata del meeting dell'utente.
   * @param immersionLevel Livello di immersività dell'utente.
   * @throws ServerRuntimeException Eccezione generata da errori server.
   */
  public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel)
      throws ServerRuntimeException;
}
