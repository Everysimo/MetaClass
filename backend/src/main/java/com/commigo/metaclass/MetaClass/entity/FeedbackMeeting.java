package com.commigo.metaclass.MetaClass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackMeeting {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime Data;

    @Column
    @NotNull(message = "Il tempo non può essere nullo")
    private Duration Tempo_Totale;

    //da definire la regola inizio<fine
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime DataPrimoAccesso;

    @NotNull(message = "La data dell'ultimo accesso non può essere nulla")
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

    //costruttore avviato alla prima istanziazione del feedback
    public FeedbackMeeting(Utente utente, Meeting meeting, Report report){
         this.utente = utente;
         this.meeting = meeting;
         this.report = report;
         this.Tempo_Totale = Duration.ZERO;
         this.DataUltimoAccesso = LocalDateTime.now();
    }

}
