package com.commigo.metaclass.MetaClass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@IdClass(value = StatoPartecipazione.class)
public class StatoPartecipazione implements Serializable {


    /**
     * Costante per valore intero di 50.
     */
    public static final int MAX_NAME_LENGTH = 50;

    /**
     *Chiave Esterna sulla stanza
     */
    @Id
    @NotNull(message = "La stanza non può essere nulla")
    @ManyToOne()
    @JoinColumn(name = "id_stanza")
    private Stanza stanza;

    /**
     *Chiave Esterna sull'utente
     */
    @Id
    @NotNull(message = "L'utente non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_utente")
    private Utente utente;

    /**
     *Chiave Esterna sulla ruolo dell'utente
     */
    @NotNull(message = "Il ruolo  non può essere nullo")
    @ManyToOne()
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
    @Size(min = 1, max = MAX_NAME_LENGTH, message = "Lunghezza del NomeInStanza non valida")
    @NotBlank(message = "il nome nella stanza non può essere vuota")
    private String NomeInStanza;

    //data creazione e aggiornamento dei dati
    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime Data_Creazione;

    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime Data_Aggiornamento;

    public StatoPartecipazione(Stanza stanza, Utente utente, Ruolo ruolo,
                               boolean isInAttesa, boolean isBannato, String nomeInStanza) {
        this.stanza = stanza;
        this.utente = utente;
        this.ruolo = ruolo;
        this.isInAttesa = isInAttesa;
        this.isBannato = isBannato;
        NomeInStanza = nomeInStanza;
    }
}
