package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.exceptions.DataFormatException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meeting {
    /**
     * Costante per valore intero di 254.
     */
    public static final int MAX_NAME_LENGTH = 254;

    /**
     * Costante per valore intero di 2.
     */
    public static final int MIN_NAME_LENGTH = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Il nome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza nome non valida")
    @NotBlank(message = "Il nome non può essere vuoto")
    @Pattern(regexp = "^[A-Z][A-Za-z0-9\\s]*$",
            message = "Il nome deve iniziare con una lettera maiuscola seguita da lettere " +
                      "minuscole senza spazi o caratteri speciali")
    private String nome;

    @NotNull(message = "L'inizio non può essere nullo")
    @Future(message = "l'inizio deve essere successiva alla data odierna")
    private LocalDateTime inizio;

    @NotNull(message = "La fine non può essere nulla")
    @Future(message = "la fine deve essere successiva alla data odierna")
    private LocalDateTime fine;

    @NotNull(message = "isAvviato non può essere nullo")
    private boolean isAvviato;

    /**
     *Chiave Esterna sullo Scenario
     */
    @NotNull(message = "Lo scenario non può essere nullo")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "id_scenario",  nullable = true)
    private Scenario scenario_iniziale;

    /**
     *Chiave Esterna sulla stanza
     */
    @NotNull(message = "Lo stanza non può essere nulla")
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_stanza")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stanza stanza;

    @AssertTrue(message = "L'inizio deve essere precedente alla fine")
    public boolean isStartDateBeforeEndDate() {
        // La validazione sarà passata solo se la data di inizio è precedente a quella di fine
        return inizio == null || fine == null || inizio.isBefore(fine);
    }

    @JsonCreator(mode = JsonCreator.Mode.DEFAULT)
    public Meeting(@JsonProperty("nome") String Nome,
                   @JsonProperty("inizio") String Inizio,
                   @JsonProperty("fine") String Fine,
                   @JsonProperty("id_stanza") Long stanza,
                   @JsonProperty("id_meeting") Long meeting) throws DataFormatException {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (meeting != null) {
            this.id = meeting;
        }

        this.nome = Nome;

        try{
           this.inizio = LocalDateTime.parse(Inizio,formatter);
        }catch(DateTimeParseException ex){
            throw new DataFormatException("Formato 'inizio' non valido (yyyy-MM-dd HH:mm)");
        }

        try{
            this.fine = LocalDateTime.parse(Fine,formatter);
        }catch(DateTimeParseException ex){
            throw new DataFormatException("Formato 'fine' non valido (yyyy-MM-dd HH:mm)");
        }

        this.isAvviato = false;

        this.stanza = new Stanza();
        this.stanza.setId(stanza);

        this.scenario_iniziale = new Scenario();
    }

}
