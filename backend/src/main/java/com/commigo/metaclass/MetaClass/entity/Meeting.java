package com.commigo.metaclass.MetaClass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    private long Id;

    @NotNull(message = "Il nome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza nome non valida")
    @NotBlank(message = "Il nome non può essere vuoto")
    private String Nome;

    //da definire la regola inizio<fine
    @NotNull(message = "L'inizio non può essere nullo")
    @Past(message = "La data di nascita non può essere successiva "+ "o coincidente alla data odierna")
    @NotBlank(message = "L'inizio non può essere vuoto")
    private LocalDateTime Inizio;

    @NotNull(message = "La fine non può essere nulla")
    @Past(message = "La data di nascita non può essere successiva "+ "o coincidente alla data odierna")
    @NotBlank(message = "La fine non può essere vuota")
    private LocalDateTime Fine;

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



}
