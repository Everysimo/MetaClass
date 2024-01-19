package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.exceptions.MismatchJsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

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
    @Pattern(regexp = "^[A-Z][A-Za-z0-9\\s]*$",
            message = "Formato nome errato")
    @NotBlank (message = "Il nome non può essere vuoto")
    private String nome;


    @NotBlank (message = "La descrizione non puo' essere vuota")
    @NotNull(message = "La descrizione non puo' essere nulla")
    @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "Lunghezza della descrizione non valida")
    @Pattern(regexp="^[A-Z][a-zA-Z0-9.,!?()'\"\\-\\s]+$",
            message = "Formato descrizione errato")
    private String descrizione;

    private float media_valutazione;

    @NotNull(message = "Il numero dei voti non può mai essere nullo")
    private int num_voti = 0;

    /**
     *Chiave Esterna sulla Categoria
     */
    @NotNull(message = "La categoria non può essere nulla")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_categoria")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Categoria categoria;

    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime data_creazione;

    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime data_aggiornamento;

    @NotNull(message = "L'URL dell'immagine non può essere nullo")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Immagine image;

    @JsonCreator
    public Scenario(@JsonProperty("nome") String Nome,
                    @JsonProperty("descrizione") String Descrizione,
                    @JsonProperty("url_immagine") @URL String url ,
                    @JsonProperty("id_categoria") Long idCategoria) throws MismatchJsonProperty {

        if (Nome == null || Descrizione == null || url == null || idCategoria == null) {
            throw new MismatchJsonProperty("gli attributi non sono corretti");
        }

        this.nome = Nome;
        this.descrizione = Descrizione;
        this.image = new Immagine();
        this.image.setUrl(url);
        this.categoria = new Categoria();
        this.categoria.setId(idCategoria);

    }

    //costruttore creato a fini di testing
    public Scenario(Long Id, String Nome, String Descrizione, Immagine immagine ,Categoria cat)  {

        this.id = Id;
        this.nome = Nome;
        this.descrizione = Descrizione;
        this.image = immagine;
        this.categoria = cat;

    }
}
