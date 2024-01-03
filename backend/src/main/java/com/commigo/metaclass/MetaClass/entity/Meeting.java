package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"id_stanza"})
public class Meeting {

    /**
     * Costante per valore intero di 254.
     */
    public static final int MAX_NAME_LENGTH = 254;

    /**
     * Costante per valore intero di 1.
     */
    public static final int MIN_NAME_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Il nome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza nome non valida")
    //@NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    //da definire la regola inizio<fine
    @NotNull(message = "L'inizio non può essere nullo")
    @Past(message = "La data di nascita non può essere successiva "+ "o coincidente alla data odierna")
    @NotBlank(message = "L'inizio non può essere vuoto")
    private LocalDateTime inizio;

    @NotNull(message = "La fine non può essere nulla")
    @Past(message = "La data di nascita non può essere successiva "+ "o coincidente alla data odierna")
    @NotBlank(message = "La fine non può essere vuota")
    private LocalDateTime fine;

    /**
     *Chiave Esterna sullo Scenario
     */
    @NotNull(message = "Lo scenario non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_scenario")
    private Scenario scenario_iniziale;

    /**
     *Chiave Esterna sulla stanza
     */
    @NotNull(message = "Lo stanza non può essere nulla")
    @ManyToOne()
    @JoinColumn(name = "id_stanza")
    private Stanza stanza;

    @JsonCreator
    public Meeting(@JsonProperty("nome") String Nome,
                   @JsonProperty("inizio") String Inizio,
                   @JsonProperty("fine") String Fine){

        this.nome = Nome;

        // Definire il formato della stringa
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        this.inizio = LocalDateTime.parse(Inizio.replace("T"," "), formatter);
        this.fine = LocalDateTime.parse(Fine.replace("T"," "), formatter);

    }


}
