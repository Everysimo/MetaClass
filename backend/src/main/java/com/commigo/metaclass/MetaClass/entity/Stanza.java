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

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stanza {

    /**
     * Costante per valore intero di 50.
     */
    public static final int MAX_NAME_LENGTH = 50;

    /**
     * Costante per valore intero di 254.
     */
    public static final int MAX_DESCR_LENGTH = 254;

    /**
     * ID della stanza
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Nome della Stanza
     */
    @NotNull(message = "Il nome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = 1, max = MAX_NAME_LENGTH, message = "Lunghezza nome non valida")
    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    /**
     * Codice della Stanza
     */

    @NotNull(message = "Il codice della stanza non può essere nullo")
    @Column(length = MAX_NAME_LENGTH, unique = true)
    @Size(min = 6, max = 6, message = "Lunghezza del codice stanza non valido")
    @NotBlank(message = "Il codice stanza  non può essere vuoto")
    private String codice;

    /**
     * Descrizione della Stanza
     */
    @NotNull(message = "La descrizione della stanza non può essere nulla")
    @Column(length = MAX_DESCR_LENGTH)
    @Size(min = 1, max = MAX_DESCR_LENGTH, message = "Lunghezza della descrizione non valida")
    @NotBlank(message = "La descrizione non può essere vuota")
    private String descrizione;

    /**
     *Tipo di Accesso alla Stanza, ovvero la stanza è pubblica (1) o privata (0)
     */
    @NotNull(message = "Il tipo di accesso non può essere nullo")
    private boolean tipo_Accesso;

    /**
     *Identifica il numero massimo di posti nella stanza
     */
    @NotNull(message = "Il numero massimo di posti non può essere nullo")
    @Min(value = 1, message = "Il valore del  parametro non deve essere inferiore ad 1")
    @Max(value = 999, message = "Il valore del  parametro non deve superare 999")
    @NotBlank(message = "Il numero massimo dei posti non può essere vuota")
    private int max_Posti;

    /**
     * Chiave Esterna sullo Scenario
     */
    @NotNull(message = "Lo scenario non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_scenario")
    private Scenario scenario;

    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime data_Creazione;

    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime data_Aggiornamento;

    @JsonCreator
    public Stanza(@JsonProperty("nome") String nome,
                  @JsonProperty("codiceStanza") String codiceStanza,
                  @JsonProperty("descrizione") String descrizione,
                  @JsonProperty("tipoAccesso") boolean tipoAccesso,
                  @JsonProperty("maxPosti") int maxPosti)

    {
        this.nome = nome;
        this.codice = codiceStanza;
        this.descrizione = descrizione;
        this.tipo_Accesso = tipoAccesso;
        this.max_Posti = maxPosti;
    }
}
