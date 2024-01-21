package com.commigo.metaclass.entity;

import com.commigo.metaclass.exceptions.DataFormatException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/** Entità Utente. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Utente {

  /** Costante per valore intero di 254. */
  public static final int MAX_NAME_LENGTH = 254;

  /** Costante per valore intero di 3. */
  public static final int MIN_NAME_LENGTH = 3;

  /** Lunghezza campo sesso. */
  public static final int SEX_LENGTH = 1;

  /** Costante per valore intero di 114. */
  public static final int MAX_ETA_LENGTH = 114;

  /** Costante per valore intero di 10. */
  public static final int MIN_ETA_LENGTH = 10;

  /** Costante per valore intero di 10. */
  private static final int MAX_PHONE_LENGTH = 10;

  /** Costante per valore intero di 10. */
  private static final int MIN_TOKEN_LENGTH = 1;

  /** Costante per valore intero di 10. */
  private static final int MAX_TOKEN_LENGTH = 1024;

  public static final String DEFAULT_TOKEN = "TODO";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  @Setter
  private long id;

  @NotNull(message = "Il nome non può essere nullo")
  @Column(length = MAX_NAME_LENGTH)
  @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "Lunghezza nome errata")
  @Pattern(regexp = "^[A-Z][a-z]*", message = "Formato nome errato")
  @NotBlank(message = "Il nome non può essere vuoto")
  private String nome;

  @NotNull(message = "Il cognome non può essere nullo")
  @Column(length = MAX_NAME_LENGTH)
  @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "Lunghezza cognome errata")
  @Pattern(regexp = "^[A-Z][a-z]*", message = "Formato cognome errato")
  @NotBlank(message = "Il cognome non può essere vuoto")
  private String cognome;

  @NotNull(message = "Il sesso non può essere nullo")
  @Column(length = SEX_LENGTH)
  @Size(min = SEX_LENGTH, max = SEX_LENGTH, message = "Lunghezza sesso non valida")
  @NotBlank(message = "Il sesso non può essere vuoto")
  @Pattern(regexp = "^[MFO]*$", message = "Il genere deve essere 'M', 'F' o 'O'")
  private String sesso;

  @NotNull(message = "La data di nascita non può essere nulla")
  @Past(message = "La data di nascita deve essere passata")
  @DateTimeFormat(pattern = "MM/dd/yyyy")
  private LocalDate dataDiNascita;

  @NotNull(message = "IsAdmin non può essere nullo")
  private boolean isAdmin;

  @NotNull(message = "L'email non può essere nulla")
  @Email(message = "Formato email non valido")
  private String email;

  @Column(length = MAX_PHONE_LENGTH)
  @Size(min = MAX_PHONE_LENGTH, max = MAX_PHONE_LENGTH, message = "Lunghezza telefono non valida")
  @Pattern(regexp = "^[0-9]*$", message = "Formato telefono non valido")
  private String telefono;

  // da valutare la lunghezza della stringa
  @NotNull(message = "IdMeta non può essere nulla")
  @Column(length = MAX_NAME_LENGTH, unique = true)
  @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "Lunghezza IdMeta non valida")
  @NotBlank(message = "Il IdMeta non può essere vuoto")
  private String metaId;

  // da valutare la lunghezza della stringa
  @NotNull(message = "TokenAuth non può essere nulla")
  @Column(length = MAX_TOKEN_LENGTH, unique = true)
  @Size(min = MIN_TOKEN_LENGTH, max = MAX_TOKEN_LENGTH, message = "Lunghezza TokenAuth non valida")
  @NotBlank(message = "Il TokenAuth non può essere vuoto")
  private String tokenAuth;

  @Column(updatable = false)
  @CreationTimestamp
  private LocalDateTime dataCreazione;

  @UpdateTimestamp private LocalDateTime dataAggiornamento;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
  @JoinColumn(name = "report_id")
  private Report report;

  /**
   * Costruttore.
   *
   * @param nome
   * @param cognome
   * @param email
   * @param data
   * @param sesso
   * @param idMeta
   * @throws DataFormatException
   */
  @JsonCreator(mode = JsonCreator.Mode.DEFAULT)
  public Utente(
      @JsonProperty("nome") String nome,
      @JsonProperty("cognome") String cognome,
      @JsonProperty("email") String email,
      @JsonProperty("eta") String data,
      @JsonProperty("sesso") String sesso,
      @JsonProperty("metaId") String idMeta)
      throws DataFormatException {
    this.nome = nome;
    this.cognome = cognome;

    // stringa messa quando si da errore durante l'email non trovata
    if (email == null || email.isEmpty()) {
      this.email = "d.cavaliere13@studenti.unisa.it";
    } else {
      this.email = email;
    }

    this.metaId = idMeta;
    this.sesso = sesso;

    if (data != null && !data.isEmpty()) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        this.dataDiNascita = LocalDate.parse(data, formatter);
      } catch (DateTimeParseException e) {
        throw new DataFormatException(
            "Formato della data di nascita non valido. Formato richiesto: MM/dd/yyyy");
      }
    }
  }

  /**
   * Costruttore.
   *
   * @param id
   * @param nome
   * @param cognome
   * @param email
   * @param data
   * @param sesso
   * @param idMeta
   * @param token
   * @param isAdmin
   * @throws DataFormatException
   */
  public Utente(
      Long id,
      String nome,
      String cognome,
      String email,
      String data,
      String sesso,
      String idMeta,
      String token,
      boolean isAdmin)
      throws DataFormatException {
    this.id = id;
    this.nome = nome;
    this.cognome = cognome;
    this.email = email;
    this.metaId = idMeta;
    this.sesso = sesso;
    this.tokenAuth = token;
    this.isAdmin = isAdmin;

    if (data != null && !data.isEmpty()) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        this.dataDiNascita = LocalDate.parse(data, formatter);
      } catch (DateTimeParseException e) {
        throw new DataFormatException(
            "Formato della data di nascita non valido. Formato richiesto: MM/dd/yyyy");
      }
    }
  }
}
