package com.commigo.metaclass.entity;

import com.commigo.metaclass.exceptions.MismatchJsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/** Entità stanza. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stanza {

  /** Costante per valore intero di 2. */
  public static final int MIN_NAME_LENGTH = 2;

  /** Costante per valore intero di 254. */
  public static final int MAX_NAME_LENGTH = 254;

  /** Costante per valore intero di 254. */
  public static final int MAX_DESCR_LENGTH = 254;

  /** ID della stanza. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** Nome della Stanza. */
  @NotNull(message = "Il nome non può essere nullo")
  @Column(length = MAX_NAME_LENGTH)
  @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "Lunghezza nome errata")
  @Pattern(regexp = "^[A-Z][a-zA-Z0-9\\s]*$", message = "Formato nome errato")
  @NotBlank(message = "Il nome non può essere vuoto")
  private String nome;

  /** Codice della Stanza. */
  @Column(length = MAX_NAME_LENGTH, unique = true)
  @Size(min = 6, max = 6, message = "Lunghezza codice_stanza errato")
  @Pattern(regexp = "^[0-9]*$", message = "Formato codice_stanza errato")
  private String codice;

  /** Descrizione della Stanza. */
  @NotNull(message = "La descrizione della stanza non può essere nulla")
  @Column(length = MAX_DESCR_LENGTH)
  @Size(min = MIN_NAME_LENGTH, max = MAX_DESCR_LENGTH, message = "Lunghezza descrizione errata")
  @Pattern(regexp = "^[A-Z][a-zA-Z0-9.,!?()'\"\\-\\s]*$", message = "Formato descrizione errata")
  @NotBlank(message = "La descrizione non può essere vuota")
  private String descrizione;

  /** Tipo di Accesso alla Stanza, ovvero la stanza è pubblica (1) o privata (0). */
  @NotNull(message = "Il tipo di accesso non può essere nullo")
  private boolean tipoAccesso;

  /** Identifica il numero massimo di posti nella stanza. */
  @NotNull(message = "Il numero massimo di posti non può essere nullo")
  @Min(value = 1, message = "Il valore del  parametro non deve essere inferiore ad 1")
  @Max(value = 999, message = "Il valore del  parametro non deve superare 999")
  private int maxPosti;

  /** Chiave Esterna sullo Scenario. */
  @NotNull(message = "Lo scenario non può essere nullo")
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "id_scenario")
  private Scenario scenario;

  @Column(name = "Data_Creazione", updatable = false)
  @CreationTimestamp
  private LocalDateTime dataCreazione;

  @Column(name = "Data_Aggiornamento")
  @UpdateTimestamp
  private LocalDateTime dataAggiornamento;

  /**
   * Costruttore.
   *
   * @param nome Nome stanza.
   * @param descrizione Descrizione stanza.
   * @param tipoAccesso Stanza pubblica o privata.
   * @param maxPosti Intero con massimo dei posti in stanza
   * @param idScenario Id scenario.
   * @throws MismatchJsonProperty Eccezione generata quando la validazione json fallisce.
   */
  @JsonCreator
  public Stanza(
      @JsonProperty("nome") String nome,
      @JsonProperty("descrizione") String descrizione,
      @JsonProperty("tipoAccesso") boolean tipoAccesso,
      @JsonProperty("maxPosti") Object maxPosti,
      @JsonProperty("id_scenario") Long idScenario)
      throws MismatchJsonProperty {

    if (nome == null || descrizione == null || idScenario == null) {
      throw new MismatchJsonProperty("gli attributi non sono corretti");
    }

    if (maxPosti instanceof Integer) {
      this.maxPosti = (int) maxPosti;
    } else {
      throw new MismatchJsonProperty("maxPosti non è un intero");
    }

    this.nome = nome;
    this.descrizione = descrizione;
    this.tipoAccesso = tipoAccesso;

    // aggiunta dello scenario
    this.scenario = new Scenario();
    this.scenario.setId(idScenario);
  }

  /**
   * Costruttore.
   *
   * @param nome Nome stanza.
   * @param descrizione Dscrizione stanza.
   * @param tipoAccesso Stanza pubblica o privata.
   * @param maxPosti Intero con massimo dei posti in stanza
   * @param scenario scenario.
   * @param codice Codice stanza.
   * @throws MismatchJsonProperty Eccezione generata quando la validazione json fallisce.
   */
  public Stanza(
      Long id,
      String nome,
      String descrizione,
      boolean tipoAccesso,
      int maxPosti,
      Scenario scenario,
      String codice)
      throws MismatchJsonProperty {

    if (nome == null || descrizione == null) {
      throw new MismatchJsonProperty("gli attributi non sono corretti");
    }

    this.nome = nome;
    this.descrizione = descrizione;
    this.tipoAccesso = tipoAccesso;
    this.maxPosti = (maxPosti > 0) ? maxPosti : 1;
    this.codice = codice;
    // aggiunta dello scenario
    this.scenario = scenario;
  }
}
