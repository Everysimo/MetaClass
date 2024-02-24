package com.commigo.metaclass.gestionestimaduratameeting.adapter;

import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import java.time.Duration;

/** Interfaccia che descrive i metodi dell'adapter. */
public interface StimaDurataMeetingAdapter {

  /**
   * Metodo che permette di aggiungere le informazioni di un utente nel dataset.
   *
   * @param u utente di cui si vogliono aggiungere le informazioni all'interno del dataset.
   * @param durata Durata dell'utente nel meeting.
   * @param immersionLevel Livello di quanto è stato immerso l'utente nel meeting.
   * @param motionSickness Livello di fastidio.
   * @throws ServerRuntimeException Eccezione generata per errori del server.
   */
  public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel, int motionSickness)
      throws ServerRuntimeException;
}
