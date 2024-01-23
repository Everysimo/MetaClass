package com.commigo.metaclass.gestionestanza.repository;

import com.commigo.metaclass.entity.Stanza;
import jakarta.transaction.Transactional;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository della stanza per gestire transazioni con i dati persistenti. */
@Repository("StanzaRepository")
public interface StanzaRepository extends JpaRepository<Stanza, Long> {

  /**
   * Metodo che permette la ricerca di una stanza in base al suo codice.
   *
   * @param codice codice su cui si base la ricerca
   * @return stanza con il codice inserito
   */
  Stanza findStanzaByCodice(String codice);

  /**
   * Metodo che permette la ricerca di una stanza in base a un id.
   *
   * @param id id su cui si basa la ricerca
   * @return stanza con l'id inserito
   */
  Stanza findStanzaById(long id);

  /**
   * Metodo che permette la ricerca dell'ultima stanza creata.
   *
   * @return l'id dell'ultima stanza inserita
   */
  @Query("SELECT s.id FROM Stanza s ORDER BY s.id DESC LIMIT 1")
  Long findIdUltimaTupla();

  // il valore restituito indica il numero di righe modificate, quindi:
  // 1 se tutto ok
  // 0 se non è ok
  /**
   * Metodo che permette di modificare i dati di una stanza.
   *
   * @param id id della stanza da modificare
   * @param attributes i nuovi dati della stanza
   * @return il numero di colonne modificate
   */
  @Modifying
  @Transactional
  @Query(
      "UPDATE Stanza stanza SET "
          + "stanza.nome = COALESCE(:#{#attributes['nome']}, stanza.nome), "
          + "stanza.codice = COALESCE(:#{#attributes['codice']}, stanza.codice), "
          + "stanza.descrizione = COALESCE(:#{#attributes['descrizione']}, stanza.descrizione), "
          + "stanza.tipoAccesso = COALESCE(:#{#attributes['tipo_Accesso']}, stanza.tipoAccesso), "
          + "stanza.maxPosti = COALESCE(:#{#attributes['max_Posti']}, stanza.maxPosti), "
          + "stanza.scenario.id = COALESCE(:#{#attributes['id_scenario']}, stanza.scenario.id) "
          + "WHERE stanza.id = :Id")
  int updateAttributes(@Param("Id") Long id, @Param("attributes") Map<String, Object> attributes);
}
