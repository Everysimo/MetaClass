package com.commigo.metaclass.entity;

import com.commigo.metaclass.utility.multipleid.UtenteInMeetingId;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/** UtenteInMeeting. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UtenteInMeetingId.class)
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
  @JoinColumn(name = "idMeeting")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Meeting meeting;

  @NotNull(message = "IsOnline non può essere nullo")
  private boolean isOnline;
}
