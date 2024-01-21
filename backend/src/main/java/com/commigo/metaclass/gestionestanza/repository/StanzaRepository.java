package com.commigo.metaclass.gestionestanza.repository;

import com.commigo.metaclass.entity.Stanza;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository("StanzaRepository")
public interface StanzaRepository extends JpaRepository<Stanza, Long> {

  /**
   * metodo che permette la ricerca di una stanza in base al suo codice
   *
   * @param codice codice su cui si base la ricerca
   * @return stanza con il codice inserito
   */
  Stanza findStanzaByCodice(String codice);

  /**
   * metodo che permette la ricerca di una stanza in base ad un id
   *
   * @param id id su cui si basa la ricerca
   * @return stanza con l'id inserito
   */
  Stanza findStanzaById(long id);

  /**
   * meotodo che permette la ricerca dell'ultima stanza creata
   *
   * @return l'id dell'ultima stanza inserita
   */
  @Query("SELECT s.id FROM Stanza s ORDER BY s.id DESC LIMIT 1")
  Long findIdUltimaTupla();

  // il valore restituito indica il numero di righe modificate, quindi:
  // 1 se tutto ok
  // 0 se non è ok
  /**
   * metodo che permette di modificare i dati di una stanza
   *
   * @param Id id della stanza da modificare
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
          + "stanza.tipo_Accesso = COALESCE(:#{#attributes['tipo_Accesso']}, stanza.tipo_Accesso), "
          + "stanza.max_Posti = COALESCE(:#{#attributes['max_Posti']}, stanza.max_Posti), "
          + "stanza.scenario.id = COALESCE(:#{#attributes['id_scenario']}, stanza.scenario.id) "
          + "WHERE stanza.id = :Id")
  int updateAttributes(@Param("Id") Long Id, @Param("attributes") Map<String, Object> attributes);
}
