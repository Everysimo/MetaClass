package com.commigo.metaclass.utility.multipleid;

import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.Utente;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Stato partecipazione con id multiplo. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatoPartecipazioneId implements Serializable {

  private Utente utente;
  private Stanza stanza;
}
