package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionemeeting.controller.CustomExceptionHandler;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @NotBlank(message = "Il nome non può essere vuoto")
    @Pattern(regexp = "^[A-Z][a-z]*$",
            message = "Il nome deve iniziare con una lettera maiuscola seguita da lettere " +
                      "minuscole senza spazi o caratteri speciali")
    private String nome;

    @NotNull(message = "L'inizio non può essere nullo")
    @Future(message = "l'inizio deve essere successivo alla data odierna")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime inizio;

    @NotNull(message = "La fine non può essere nulla")
    @Future(message = "la fine deve essere successivo alla data odierna")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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

    @AssertTrue(message = "L'inizio deve essere precedente alla fine")
    public boolean isStartDateBeforeEndDate() {
        // La validazione sarà passata solo se la data di inizio è precedente a quella di fine
        return inizio == null || fine == null || inizio.isBefore(fine);
    }

    @JsonCreator
    public Meeting(@JsonProperty("nome") String Nome,
                   @JsonProperty("inizio") LocalDateTime Inizio,
                   @JsonProperty("fine")  LocalDateTime Fine,
                   @JsonProperty("id_stanza") Long stanza){

        this.nome = Nome;

        this.inizio = Inizio;
        this.fine = Fine;

        this.stanza = new Stanza();
        this.stanza.setId(stanza);

        this.scenario_iniziale = new Scenario();

    }


}
