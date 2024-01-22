package com.commigo.metaclass.entity;

import com.commigo.metaclass.utility.multipleid.StatoPartecipazioneId;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.transaction.TransactionSystemException;

/** Entità Stanza. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(StatoPartecipazioneId.class)
public class StatoPartecipazione implements Serializable {

  /** Costante per valore intero di 50. */
  public static final int MAX_NAME_LENGTH = 50;

  /** Chiave Esterna sulla stanza. */
  @Id
  @NotNull(message = "La stanza non può essere nulla")
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_stanza")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Stanza stanza;

  /** Chiave Esterna sull'utente. */
  @Id
  @NotNull(message = "L'utente non può essere nullo")
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_utente")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Utente utente;

  /** Chiave Esterna sulla ruolo dell'utente. */
  @NotNull(message = "Il ruolo  non può essere nullo")
  @ManyToOne(cascade = {CascadeType.MERGE})
  @JoinColumn(name = "id_ruolo")
  private Ruolo ruolo;

  /** isInAttesa per verificare se l'utente è in attesa di entrare nella stanza. */
  @NotNull(message = "isInAttesa non può essere nullo")
  private boolean isInAttesa;

  /** isBannato per verificare se l'utente è stato bannato da una stanza. */
  @NotNull(message = "isBannato non può essere nullo")
  private boolean isBannato;

  /** isSilenziato per verificare se l'utente è silenziato in una stanza. */
  @NotNull(message = "isSilenziato non può essere nullo")
  private boolean isSilenziato;

  /** NomeInStanza identifica il nome dell'utente nella stanza specifica. */
  @NotNull(message = "Il nome nella stanza non può essere nullo")
  @Column(length = MAX_NAME_LENGTH)
  @Pattern(regexp = "^[A-Z][A-Za-z0-9]*$")
  @Size(min = 1, max = MAX_NAME_LENGTH, message = "Lunghezza del NomeInStanza non valida")
  @NotBlank(message = "Il nome nella stanza non può essere vuota")
  private String nomeInStanza;

  // data creazione e aggiornamento dei dati
  @Column(name = "Data_Creazione", updatable = false)
  @CreationTimestamp
  private LocalDateTime dataCreazione;

  @Column(name = "Data_Aggiornamento")
  @UpdateTimestamp
  private LocalDateTime dataAggiornamento;

  /**
   * Metodo che controlla il ruolo di admin.
   */
  public void checkRule() {
    try {
      if (this.utente.isAdmin() && this.isBannato) {
        throw new TransactionSystemException(
            "un'amministratore se viene bannato " + "se lo può recovare!");
      }

      if (!this.ruolo.getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)) {
        if (this.isInAttesa) {

          throw new TransactionSystemException(
              "non puoi inserire un ruolo " + "diverso da partecipante che sia in attesa");

        } else if (this.ruolo.getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
            && this.isBannato) {

          throw new TransactionSystemException(
              "L'organizzatore master non può " + "essere inserito come bannato");
        }
      }
    } catch (TransactionSystemException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Costruttore.
   *
   * @param stanza Istanza della stanza.
   * @param utente Istanza dell'utente.
   * @param ruolo Istanza del ruolo.
   * @param isInAttesa Verifica se l'utente è in attesa di entrare in stanza.
   * @param isBannato Verifica se all'utente è vietato l'accesso.
   * @param nomeInStanza Nome dell'utente nella stanza.
   * @param isSilenziato Verifica se l'utente è silenziato.
   * @throws TransactionSystemException Eccezione se la transazione non è stata effettuata.
   */
  public StatoPartecipazione(
      Stanza stanza,
      Utente utente,
      Ruolo ruolo,
      boolean isInAttesa,
      boolean isBannato,
      String nomeInStanza,
      boolean isSilenziato)
      throws TransactionSystemException {
    this.stanza = stanza;
    this.utente = utente;
    this.ruolo = ruolo;
    this.isInAttesa = isInAttesa;
    this.isBannato = isBannato;
    this.nomeInStanza = nomeInStanza;
    this.isSilenziato = isSilenziato;
    checkRule();
  }
}
