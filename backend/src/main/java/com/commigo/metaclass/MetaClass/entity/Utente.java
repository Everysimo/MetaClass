package com.commigo.metaclass.MetaClass.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"isLoggedIn"})
public class Utente {

    /**
     * Costante per valore intero di 254.
     */
    public static final int MAX_NAME_LENGTH = 254;

    /**
     * Costante per valore intero di 1.
     */
    public static final int MIN_NAME_LENGTH = 1;

    /**
     * Lunghezza campo sesso.
     */
    public static final int SEX_LENGTH = 1;

    /**
     * Costante per valore intero di 114.
     */
    public static final int MAX_ETA_LENGTH = 114;

    /**
     * Costante per valore intero di 10.
     */
    public static final int MIN_ETA_LENGTH = 10;

    /**
     * Costante per valore intero di 10.
     */
    private static final int MAX_PHONE_LENGTH = 10;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;


    @NotNull(message = "Il nome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza nome non valida")
    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    @NotNull(message = "Il cognome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza cognome non valida")
    @NotBlank(message = "Il cognome non può essere vuoto")
    private String cognome;

    @NotNull(message = "Il sesso non può essere nullo")
    @Column(length = SEX_LENGTH)
    @Size(min = SEX_LENGTH, max = SEX_LENGTH,
            message = "Lunghezza sesso non valida")
    @NotBlank(message = "Il sesso non può essere vuoto")
    @Pattern(regexp = "^[MFO]$", message = "Il genere deve essere 'M', 'F' o 'O'")
    private String sesso;

    @NotNull(message = "L'età non può essere nulla")
    @Column(length = MAX_ETA_LENGTH)
    @Min(value = MIN_ETA_LENGTH, message = "L'età deve essere maggiore o uguale a 10")
    @Max(value = MAX_ETA_LENGTH, message = "L'età deve essere minore o uguale a 114")
    private int età;

    @NotNull(message = "IsAdmin non può essere nullo")
    private boolean isAdmin;

    @NotNull(message = "L'email non può essere nulla")
    @Email(message = "Formato email non valida")
    private String email;

    @Column(length = MAX_PHONE_LENGTH)
    @Size(min = MAX_PHONE_LENGTH, max = MAX_PHONE_LENGTH,
            message = "Lunghezza telefono non valida")
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Formato telefono non valido")
    private String telefono;

    //da valutare la lunghezza della stringa
    @NotNull(message = "IdMeta non può essere nulla")
    @Column(length = MAX_NAME_LENGTH, unique = true)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza IdMeta non valida")
    @NotBlank(message = "Il IdMeta non può essere vuoto")
    private String metaId;

    //da valutare la lunghezza della stringa
    @NotNull(message = "TokenAuth non può essere nulla")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza TokenAuth non valida")
    @NotBlank(message = "Il TokenAuth non può essere vuoto")
    private String tokenAuth;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCreazione;

    @UpdateTimestamp
    private LocalDateTime dataAggiornamento;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @JsonCreator
    public Utente(@JsonProperty("nome") String Nome,
                  @JsonProperty("cognome") String Cognome,
                  @JsonProperty("email") String Email,
                  @JsonProperty("metaId") String IdMeta,
                  @JsonProperty("tokenAuth") String TokenAuth) {
        this.nome = Nome;
        this.cognome = Cognome;
        this.email = Email;
        this.metaId = IdMeta;
        this.tokenAuth = TokenAuth;
    }
}
