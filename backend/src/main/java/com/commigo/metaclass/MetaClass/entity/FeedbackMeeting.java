package com.commigo.metaclass.MetaClass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

  // da definire la regola inizio<fine
  @Column(updatable = false)
  @CreationTimestamp
  private LocalDateTime dataPrimoAccesso;

  @NotNull(message = "La data dell'ultimo accesso non può essere nulla")
  @Column
  private LocalDateTime dataUltimoAccesso;

  /** Chiave Esterna sull' utente */
  @NotNull(message = "L'utente non può essere nullo")
  @ManyToOne()
  @JoinColumn(name = "id_utente")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Utente utente;

  @NotNull(message = "Il meeting non può essere nullo")
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "id_meeting")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Meeting meeting;

  @NotNull(message = "Il report non può essere nullo")
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "id_report")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Report report;

  @Column(name = "immersionLevel")
  @NotNull(message = "Il livello di immersività non può essere mai nullo")
  @Min(value = 1, message = "Il livello di immersività non può essere inferiore a 1")
  @Max(value = 5, message = "Il livello di immersività non può essere superiore a 5")
  private int immersionLevel;

  @Column(name = "motionSickness")
  @NotNull(message = "Il motionSickness non può essere mai nullo")
  @Min(value = 1, message = "Il motionSickness non può essere inferiore a 1")
  @Max(value = 10, message = "Il motionSickness non può essere superiore a 10")
  private int motionSickness;

  @NotNull(message = "il questionario non può essere nullo")
  private boolean compiledQuestionario;

  // costruttore avviato alla prima istanziazione del feedback
  public FeedbackMeeting(Utente utente, Meeting meeting, Report report) {
    this.utente = utente;
    this.meeting = meeting;
    this.report = report;
    this.tempo_totale = Duration.ZERO;
    this.dataUltimoAccesso = LocalDateTime.now();
    this.compiledQuestionario = false;
    this.motionSickness = 1;
    this.immersionLevel = 1;
  }

  public FeedbackMeeting(
      Utente utente,
      Meeting meeting,
      Report report,
      Duration tempo_totale,
      LocalDateTime ultimo,
      boolean compiled) {
    this.utente = utente;
    this.meeting = meeting;
    this.report = report;
    this.tempo_totale = tempo_totale;
    this.dataUltimoAccesso = ultimo;
    this.compiledQuestionario = compiled;
    this.motionSickness = 1;
    this.immersionLevel = 1;
  }
}
