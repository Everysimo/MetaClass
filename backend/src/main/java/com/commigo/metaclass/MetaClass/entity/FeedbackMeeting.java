package com.commigo.metaclass.MetaClass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    private LocalDateTime data_compilazione;

    @Column
    @NotNull(message = "Il tempo non può essere nullo")
    private Duration tempo_totale;

    //da definire la regola inizio<fine
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime dataPrimoAccesso;

    @NotNull(message = "La data dell'ultimo accesso non può essere nulla")
    @Column
    private LocalDateTime dataUltimoAccesso;

    /**
     *Chiave Esterna sull' utente
     */
    @NotNull(message = "L'utente non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_utente")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utente utente;

    @NotNull(message = "Il meeting non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_meeting")
    @OnDelete(action = OnDeleteAction.CASCADE)
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
         this.tempo_totale = Duration.ZERO;
         this.dataUltimoAccesso = LocalDateTime.now();
    }

}
