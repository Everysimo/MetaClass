package com.commigo.metaclass.entity;

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

/** Entità Ruolo. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ruolo {

  public static final int MAX_NAME_LENGTH = 254;
  public static final int MIN_NAME_LENGTH = 1;

  public static final String PARTECIPANTE = "Partecipante";
  public static final String ORGANIZZATORE = "Organizzatore";
  public static final String ORGANIZZATORE_MASTER = "Organizzatore_Master";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull(message = "Il nome non può essere nullo")
  @Column(unique = true)
  @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "Lunghezza del nome non valida")
  @NotBlank(message = "il nome non può essere vuoto")
  private String nome;

  public Ruolo(String nome) {
    this.nome = nome;
  }
}
