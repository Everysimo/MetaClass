package com.commigo.metaclass.MetaClass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Utente {

    /**
     * Costante per valore intero di 50.
     */
    public static final int MAX_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @NotNull(message = "Il nome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    private String Nome;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NotSet' NOT NULL")
    private String Cognome;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT 'M' NOT NULL")
    private String Sesso;

    @Column(columnDefinition = "INT DEFAULT 0 NOT NULL")
    private int Età;

    @Column(columnDefinition = "BOOLEAN DEFAULT false NOT NULL")
    private boolean isAdmin;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'default@example.com' NOT NULL")
    private String Email;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NotSet'")
    private String Telefono;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NotSet' NOT NULL")
    private String idMeta;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NotSet' NOT NULL")
    private String TokenAuth;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL")
    private LocalDateTime DataCreazione;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL")
    private LocalDateTime DataAggiornamento;

}
