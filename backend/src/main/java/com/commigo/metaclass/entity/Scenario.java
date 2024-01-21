package com.commigo.metaclass.entity;

import com.commigo.metaclass.exceptions.MismatchJsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

/** Entità scenario. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Scenario {

  public static final int MAX_LENGTH = 254;
  public static final int MIN_LENGTH = 3;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Il nome non può essere nullo")
  @Column(unique = true)
  @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "Lunghezza del nome non valida")
  @Pattern(regexp = "^[A-Z][A-Za-z0-9\\s]*$", message = "Formato nome errato")
  @NotBlank(message = "Il nome non può essere vuoto")
  private String nome;

  @NotBlank(message = "La descrizione non puo' essere vuota")
  @NotNull(message = "La descrizione non puo' essere nulla")
  @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "Lunghezza della descrizione non valida")
  @Pattern(regexp = "^[A-Z][a-zA-Z0-9.,!?()'\"\\-\\s]+$", message = "Formato descrizione errato")
  private String descrizione;

  private float mediaValutazione;

  @NotNull(message = "Il numero dei voti non può mai essere nullo")
  private int numVoti = 0;

  /** Chiave Esterna sulla Categoria. */
  @NotNull(message = "La categoria non può essere nulla")
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_categoria")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Categoria categoria;

  @Column(name = "Data_Creazione", updatable = false)
  @CreationTimestamp
  private LocalDateTime dataCreazione;

  @Column(name = "Data_Aggiornamento")
  @UpdateTimestamp
  private LocalDateTime dataAggiornamento;

  @NotNull(message = "L'URL dell'immagine non può essere nullo")
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  private Immagine image;

  /**
   * costruttore.
   *
   * @param nome
   * @param descrizione
   * @param url
   * @param idCategoria
   * @throws MismatchJsonProperty
   */
  @JsonCreator
  public Scenario(
      @JsonProperty("nome") String nome,
      @JsonProperty("descrizione") String descrizione,
      @JsonProperty("url_immagine") String url,
      @JsonProperty("id_categoria") Long idCategoria)
      throws MismatchJsonProperty {

    if (nome == null || descrizione == null || url == null || idCategoria == null) {
      throw new MismatchJsonProperty("gli attributi non sono corretti");
    }

    this.nome = nome;
    this.descrizione = descrizione;
    this.image = new Immagine();
    this.image.setUrl(url);
    this.categoria = new Categoria();
    this.categoria.setId(idCategoria);
  }

  /**
   * costruttore testing.
   *
   * @param id
   * @param nome
   * @param descrizione
   * @param immagine
   * @param cat
   */
  public Scenario(Long id, String nome, String descrizione, Immagine immagine, Categoria cat) {

    this.id = id;
    this.nome = nome;
    this.descrizione = descrizione;
    this.image = immagine;
    this.categoria = cat;
  }
}
