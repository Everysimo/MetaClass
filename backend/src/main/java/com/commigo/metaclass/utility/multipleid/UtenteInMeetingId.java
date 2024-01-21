package com.commigo.metaclass.utility.multipleid;

import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.entity.Utente;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Utente avente id multiplo. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtenteInMeetingId implements Serializable {

  private Utente utente;
  private Meeting meeting;
}
