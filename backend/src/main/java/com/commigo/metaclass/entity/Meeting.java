package com.commigo.metaclass.entity;

import com.commigo.metaclass.exceptions.DataFormatException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/** Entità Meeting. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meeting {
  /** Costante per valore intero di 254. */
  public static final int MAX_NAME_LENGTH = 254;

  /** Costante per valore intero di 2. */
  public static final int MIN_NAME_LENGTH = 2;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull(message = "Il nome non può essere nullo")
  @Column(length = MAX_NAME_LENGTH)
  @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "Lunghezza nome non valida")
  @NotBlank(message = "Il nome non può essere vuoto")
  @Pattern(
      regexp = "^[A-Z][A-Za-z0-9\\s]*$",
      message =
          "Il nome deve iniziare con una lettera maiuscola seguita da lettere "
              + "minuscole senza spazi o caratteri speciali")
  private String nome;

  @NotNull(message = "L'inizio non può essere nullo")
  @Future(message = "l'inizio deve essere successiva alla data odierna")
  private LocalDateTime inizio;

  @NotNull(message = "La fine non può essere nulla")
  @Future(message = "la fine deve essere successiva alla data odierna")
  private LocalDateTime fine;

  @NotNull(message = "isAvviato non può essere nullo")
  private boolean isAvviato;

  /** Chiave Esterna sullo Scenario. */
  @NotNull(message = "Lo scenario non può essere nullo")
  @ManyToOne(fetch = FetchType.EAGER, optional = true)
  @JoinColumn(name = "id_scenario", nullable = true)
  private Scenario scenarioIniziale;

  /** Chiave Esterna sulla stanza. */
  @NotNull(message = "Lo stanza non può essere nulla")
  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_stanza")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Stanza stanza;

  @AssertTrue(message = "L'inizio deve essere precedente alla fine")
  public boolean isStartDateBeforeEndDate() {
    // La validazione sarà passata solo se la data di inizio è precedente a quella di fine
    return inizio == null || fine == null || inizio.isBefore(fine);
  }

  /**
   * Costruttore.
   *
   * @param nome
   * @param inizio
   * @param fine
   * @param stanza
   * @param meeting
   * @throws DataFormatException
   */
  @JsonCreator(mode = JsonCreator.Mode.DEFAULT)
  public Meeting(
      @JsonProperty("nome") String nome,
      @JsonProperty("inizio") String inizio,
      @JsonProperty("fine") String fine,
      @JsonProperty("id_stanza") Long stanza,
      @JsonProperty("idMeeting") Long meeting)
      throws DataFormatException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    if (meeting != null) {
      this.id = meeting;
    }

    this.nome = nome;

    try {
      this.inizio = LocalDateTime.parse(inizio, formatter);
    } catch (DateTimeParseException ex) {
      throw new DataFormatException("Formato 'inizio' non valido (yyyy-MM-dd HH:mm)");
    }

    try {
      this.fine = LocalDateTime.parse(fine, formatter);
    } catch (DateTimeParseException ex) {
      throw new DataFormatException("Formato 'fine' non valido (yyyy-MM-dd HH:mm)");
    }

    this.isAvviato = false;

    this.stanza = new Stanza();
    this.stanza.setId(stanza);

    this.scenarioIniziale = new Scenario();
  }
}
