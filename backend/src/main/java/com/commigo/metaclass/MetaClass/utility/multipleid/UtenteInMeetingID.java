package com.commigo.metaclass.MetaClass.utility.multipleid;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.entity.Utente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtenteInMeetingID implements Serializable {

  private Utente utente;
  private Meeting meeting;
}
