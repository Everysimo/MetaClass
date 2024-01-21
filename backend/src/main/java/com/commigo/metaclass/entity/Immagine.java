package com.commigo.metaclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Entità Immagine. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Immagine {

  /** Costante per valore intero di 50. */
  public static final int MAX_NAME_LENGTH = 254;

  /** Costante per valore intero di 1. */
  public static final int MIN_NAME_LENGTH = 1;

  /** Costante per valore intero di 50. */
  public static final int MAX_URL_LENGTH = 1024;

  /** Costante per valore intero di 1. */
  public static final int MIN_URL_LENGTH = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull(message = "Il nome non può essere nullo")
  @Column(length = MAX_NAME_LENGTH, unique = true)
  @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "Lunghezza nome non valida")
  @NotBlank(message = "Il nome non può essere vuoto")
  private String nome;

  @NotNull(message = "l'URL non può essere nullo")
  @Column(length = MAX_URL_LENGTH, unique = true)
  @Size(min = MIN_URL_LENGTH, max = MAX_URL_LENGTH, message = "Lunghezza url non valida")
  @NotBlank(message = "Il url non può essere vuoto")
  private String url;

  /**
   * Costruttore.
   *
   * @param urlString
   */
  public Immagine(String urlString) {
    String fileName = getFileNameFromUrl(urlString);

    this.url = urlString;
    this.nome = fileName;
  }

  private String getFileNameFromUrl(String urlString) {
    int lastSlashIndex = urlString.lastIndexOf('/');
    if (lastSlashIndex >= 0 && lastSlashIndex < urlString.length() - 1) {
      return urlString.substring(lastSlashIndex + 1);
    } else {
      // Nel caso in cui l'URL non contenga un nome file valido
      throw new IllegalArgumentException("L'URL non contiene un nome di file valido.");
    }
  }
}
