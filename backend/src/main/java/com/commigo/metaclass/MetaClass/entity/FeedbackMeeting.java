package com.commigo.metaclass.MetaClass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackMeeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column
    @CreationTimestamp
    private LocalDateTime Data;

    @Column
    @NotNull(message = "Il tempo non può essere nullo")
    @NotBlank
    private float Tempo_Totale;

    //da definire la regola inizio<fine
    @NotNull(message = "La data del primo accesso non può essere nulla")
    @Past(message = "La data di primo accesso non può essere successiva "+ "o coincidente alla data odierna")
    @NotBlank(message = "La data del primo accesso non può essere vuota")
    @Column
    private LocalDateTime DataPrimoAccesso;

    @NotNull(message = "La data dell'ultimo accesso non può essere nulla")
    @NotBlank(message = "La data dell'ultimo accesso non può essere vuota")
    @Column
    private LocalDateTime DataUltimoAccesso;

    /**
     *Chiave Esterna sull' utente
     */
    @NotNull(message = "L'utente non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @NotNull(message = "Il meeting non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_meeting")
    private Meeting meeting;

    @NotNull(message = "Il report non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_report")
    private Report report;

}
