package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.utility.multipleid.UtenteInMeetingID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UtenteInMeetingID.class)
public class UtenteInMeeting {

  @Id
  @NotNull(message = "L'utente non può essere nullo")
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "id_utente")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Utente utente;

  @Id
  @NotNull(message = "il meeting non può essere nullo")
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "id_meeting")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Meeting meeting;

  @NotNull(message = "IsOnline non può essere nullo")
  private boolean IsOnline;
}
