package com.commigo.metaclass.MetaClass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.URL;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Immagine {

    /**
     * Costante per valore intero di 50.
     */
    public static final int MAX_NAME_LENGTH = 254;

    /**
     * Costante per valore intero di 1.
     */
    public static final int MIN_NAME_LENGTH = 1;

    /**
     * Costante per valore intero di 50.
     */
    public static final int MAX_URL_LENGTH = 1024;

    /**
     * Costante per valore intero di 1.
     */
    public static final int MIN_URL_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Il nome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH, unique = true)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza nome non valida")
    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    @URL(message = "L'URL dell'immagine non è valido")
    @NotNull(message = "l'URL non può essere nullo")
    @Column(length = MAX_URL_LENGTH, unique = true)
    @Size(min = MIN_URL_LENGTH, max = MAX_URL_LENGTH,
            message = "Lunghezza url non valida")
    @NotBlank(message = "Il url non può essere vuoto")
    private String url;

    /**
     *Chiave Esterna sullo Scenario
     */
    @NotNull(message = "Lo scenario non può essere nullo")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "id_scenario")
    private Scenario scenario;

}
