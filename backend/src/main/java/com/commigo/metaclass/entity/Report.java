package com.commigo.metaclass.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

/** Entità Report. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull(message = "Il numero di partecipanti non può essere nullo")
  @Min(value = 1, message = "Il numero di partecipanti non può essere inferiore a 1")
  private int numPartecipanti = 1;

  @Column(name = "Durata_Meeting")
  @NotNull(message = "La durata del meeting non può essere nulla")
  private Duration durataMeeting;

  @Column(name = "MAX_Partecipanti")
  @NotNull(message = "Il numero massimo di partecipanti non può essere nullo")
  @Min(value = 1, message = "Il numero massimo di partecipanti non può essere inferiore a 1")
  private int maxPartecipanti = 1;

  @NotNull(message = "Il meeting non può essere nullo")
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "idMeeting")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Meeting meeting;

  @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
  private List<Utente> listaPartecipanti;

  @Column(name = "Data_Creazione", updatable = false)
  @CreationTimestamp
  private LocalDateTime dataCreazione;

  @Column(name = "Data_Aggiornamento")
  @UpdateTimestamp
  private LocalDateTime dataAggiornamento;

  /**
   * costruttore.
   *
   * @param meeting
   * @param ogm
   */
  public Report(Meeting meeting, Utente ogm) {
    this.meeting = meeting;
    this.listaPartecipanti = List.of(ogm);
    this.durataMeeting = Duration.ZERO;
  }

  /**
   * Costruttore.
   *
   * @param id
   * @param numPartecipanti
   * @param durataMeeting
   * @param maxPartecipanti
   * @param meeting
   * @param listaPartecipanti
   */
  public Report(
      long id,
      int numPartecipanti,
      Duration durataMeeting,
      int maxPartecipanti,
      Meeting meeting,
      List<Utente> listaPartecipanti) {
    this.id = id;
    this.numPartecipanti = numPartecipanti;
    this.durataMeeting = durataMeeting;
    this.maxPartecipanti = maxPartecipanti;
    this.meeting = meeting;
    this.listaPartecipanti = listaPartecipanti;
  }
}
