package com.commigo.metaclass.gestionestanza.repository;

import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.StatoPartecipazione;
import com.commigo.metaclass.entity.Utente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository dello stato partecipazione per gestire transazioni con i dati persistenti. */
@Repository("StatoPartecipazioneRepository")
public interface StatoPartecipazioneRepository extends JpaRepository<StatoPartecipazione, Long> {

  /**
   * Metodo che permette di ricercare lo stato di partecipazione di utente in una stanza.
   *
   * @param utente utente su cui si basa la ricerca.
   * @param stanza stanza su cui si basa la ricerca.
   * @return stato partecipazione dell'utente all'interno della stanza specificata.
   */
  StatoPartecipazione findStatoPartecipazioneByUtenteAndStanza(Utente utente, Stanza stanza);

  /**
   * Metodo che permette di ricercare tutti gli stati di partecipazione di un determinato utente.
   *
   * @param utente utente su cui si basas la ricerca.
   * @return lista di stati parteciapzione dell'utente specificato.
   */
  List<StatoPartecipazione> findAllByUtente(Utente utente);

  /**
   * Metodo che permette di ricercare tutti gli stati partecipazione riferiti a una determinata
   * stanza.
   *
   * @param id id della stanza su cui si basa la ricerca.
   * @return lista di utenti presenti che hanno fatto accesso nella stanza.
   */
  @Query(
      "SELECT sp.utente FROM StatoPartecipazione sp WHERE "
          + "sp.isInAttesa=false AND  sp.isBannato=false and sp.stanza.id = :id")
  List<Utente> findUtentiInStanza(@Param("id") Long id);

  /**
   * Metodo che permette di ricercare tutti gli utenti bannati all'interno di una stanza.
   *
   * @param id id della stanza su cui si basa la ricerca.
   * @return lista di utenti bannati all'interno della stanza specificata.
   */
  @Query(
      "SELECT sp.utente FROM StatoPartecipazione sp "
          + "WHERE sp.isInAttesa=false AND  sp.isBannato=true and sp.stanza.id = :id")
  List<Utente> findUtentiBannatiInStanza(@Param("id") Long id);

  /**
   * Metodo che permette di ricercare tutti gli utenti in attessa all'interno di una determinata
   * stanza.
   *
   * @param id id della stanza su cui si basa la ricerca
   * @return lista di utenti in attesa nella stanza specificata
   */
  @Query(
      "SELECT sp.utente FROM StatoPartecipazione sp "
          + "WHERE sp.isInAttesa=true AND  sp.isBannato=false and sp.stanza.id = :id")
  List<Utente> findUtentiInAttesaInStanza(@Param("id") Long id);

  /**
   * Metodo che permette di eliminare tutti gli stati partecipazioni di una determinata stanza.
   *
   * @param s stanza su cui si basa la ricerca.
   */
  void deleteAllByStanza(Stanza s);

  /**
   * Metodo che ritorna l'insieme degli stati partecipazione della stanza.
   *
   * @param stanza istanza della stanza
   * @return lista di stati partecipazione
   */
  List<StatoPartecipazione> findAllByStanza(Stanza stanza);
}
