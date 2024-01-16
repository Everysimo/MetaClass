package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.exceptions.DataFormatException;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.MeetingRepository;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime inizio;

    @NotNull(message = "La fine non può essere nulla")
    @Future(message = "la fine deve essere successiva alla data odierna")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fine;

    @NotNull(message = "isAvviato non può essere nullo")
    private boolean isAvviato;

    /**
     *Chiave Esterna sullo Scenario
     */
    @NotNull(message = "Lo scenario non può essere nullo")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
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
                   @JsonProperty("inizio") LocalDateTime Inizio,
                   @JsonProperty("fine") LocalDateTime Fine,
                   @JsonProperty("id_stanza") Long stanza,
                   @JsonProperty("id_meeting") Long meeting) {


          if (meeting != null) {
              this.id = meeting;
          }
          this.nome = Nome;
          this.inizio = Inizio;
          this.fine = Fine;

          this.isAvviato = false;

          this.stanza = new Stanza();
          this.stanza.setId(stanza);

          this.scenario_iniziale = new Scenario();
    }

    public Meeting(String nome, LocalDateTime inizio, LocalDateTime fine, boolean isAvviato, Scenario scenario_iniziale, Stanza stanza) {
        this.nome = nome;
        this.inizio = inizio;
        this.fine = fine;
        this.isAvviato = isAvviato;
        this.scenario_iniziale = scenario_iniziale;
        this.stanza = stanza;
    }
}
