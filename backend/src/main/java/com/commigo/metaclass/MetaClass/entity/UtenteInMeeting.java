package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.utility.multipleid.UtenteInMeetingID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UtenteInMeetingID.class)
public class UtenteInMeeting {

    @Id
    @NotNull(message = "L'utente non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @Id
    @NotNull(message = "il meeting non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_meeting")
    private Meeting meeting;

    @NotNull(message = "IsOnline non può essere nullo")
    private boolean IsOnline;
}
