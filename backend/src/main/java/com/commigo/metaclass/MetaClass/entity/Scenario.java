package com.commigo.metaclass.MetaClass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @NotBlank
    private String nome;

    @NotNull(message = "La descrizione non può essere nulla")
    @Column
    @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "Lunghezza della descrizione non valida")
    @NotBlank
    private String descrizione_scenario;

    @NotNull(message = "la media non può essere nulla")
    @Size(min = 1, max = 5, message = "Lunghezza della media non valida")
    @NotBlank
    private float media_valutazione;

    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime data_creazione;

    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime data_aggiornamento;
}
