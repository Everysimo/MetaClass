package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.utility.multipleid.StatoPartecipazioneId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(StatoPartecipazioneId.class)
public class StatoPartecipazione implements Serializable {


    /**
     * Costante per valore intero di 50.
     */
    public static final int MAX_NAME_LENGTH = 50;

    /**
     *Chiave Esterna sulla stanza
     */
    @Id
    //@NotNull(message = "La stanza non può essere nulla")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_stanza")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stanza stanza;

    /**
     *Chiave Esterna sull'utente
     */
    @Id
    @NotNull(message = "L'utente non può essere nullo")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_utente")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utente utente;

    /**
     *Chiave Esterna sulla ruolo dell'utente
     */
    @NotNull(message = "Il ruolo  non può essere nullo")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "id_ruolo")
    private Ruolo ruolo;

    /**
     *isInAttesa per verificare se l'utente è in attesa di entrare nella stanza
     */
    @NotNull(message = "isInAttesa non può essere nullo")
    private boolean isInAttesa;

    /**
     *isBannato per verificare se l'utente è stato bannato da una stanza
     */
    @NotNull(message = "isBannato non può essere nullo")
    private boolean isBannato;

    /**
     *NomeInStanza identifica il nome dell'utente nella stanza specifica
     */
    @NotNull(message = "Il nome nella stanza non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Pattern(regexp = "^[A-Z][A-Za-z0-9]*$")
    @Size(min = 1, max = MAX_NAME_LENGTH, message = "Lunghezza del NomeInStanza non valida")
    @NotBlank(message = "Il nome nella stanza non può essere vuota")
    private String nomeInStanza;

    //data creazione e aggiornamento dei dati
    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime data_Creazione;

    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime data_Aggiornamento;

    public StatoPartecipazione(Stanza stanza, Utente utente, Ruolo ruolo,
                               boolean isInAttesa, boolean isBannato, String nomeInStanza) {
        this.stanza = stanza;
        this.utente = utente;
        this.ruolo = ruolo;
        this.isInAttesa = isInAttesa;
        this.isBannato = isBannato;
        this.nomeInStanza = nomeInStanza;
    }

}
