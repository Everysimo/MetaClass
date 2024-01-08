package com.commigo.metaclass.MetaClass.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scenario {

    public static final int MAX_LENGTH = 254;
    public static final int MIN_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Il nome non può essere nullo")
    @Column(unique = true)
    @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "Lunghezza del nome non valida")
    @NotBlank (message = "Il nome non può essere vuoto")
    private String nome;


    @NotNull(message = "La descrizione non può essere nulla")
    @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "Lunghezza della descrizione non valida")
    @NotBlank(message = "La descrizione non può essere vuoto")
    private String descrizione;

    @DecimalMin(value = "1", message = "Il valore della media deve essere almeno 1")
    @DecimalMax(value = "5", message = "Il valore della media non può superare 5")
    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 1.0")
    private float media_valutazione = 1;

    /**
     *Chiave Esterna sulla Categoria
     */
    @NotNull(message = "La categoria non può essere nulla")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime data_creazione;

    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime data_aggiornamento;

    @NotNull(message = "L'URL dell'immagine non può essere nullo")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private Immagine image;

    @JsonCreator
    public Scenario(@JsonProperty("nome") String Nome,
                    @JsonProperty("descrizione") String Descrizione,
                    @JsonProperty("id_immagine") Long idImmagine,
                    @JsonProperty("id_categoria") Long idCategoria){
        this.nome = Nome;
        this.descrizione = Descrizione;
        this.image = new Immagine();
        this.image.setId(idImmagine);
        this.categoria = new Categoria();
        this.categoria.setId(idCategoria);

    }
}
