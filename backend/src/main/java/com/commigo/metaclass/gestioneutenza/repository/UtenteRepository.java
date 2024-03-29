package com.commigo.metaclass.gestioneutenza.repository;

import com.commigo.metaclass.entity.Utente;
import jakarta.transaction.Transactional;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository dell'utente per gestire transazioni con i dati persistenti. */
@Repository("UtenteRepository")
public interface UtenteRepository extends JpaRepository<Utente, Long> {

  /**
   * Metodo che consente la ricerca di un utente tramite un metaId.
   *
   * @param idMeta metaId dell'utente da ricercare.
   * @return Utente dato un meta Id.
   */
  Utente findFirstBymetaId(String idMeta);

  /**
   * Metodo che consente la ricerca di un utente tramite un id.
   *
   * @param id id dell'utente dal ricercare
   * @return Utente dato un id.
   */
  Utente findUtenteById(long id);

  // il valore restituito indica il numero di righe modificate, quindi:
  // 1 se tutto ok
  // 0 se non è ok
  /**
   * Metodo che consente la modifica dei dati di un utente.
   *
   * @param sessionId Meta id.
   * @param attributes i nuovi dati dell'utente
   * @return aggiorna utente presente nel database.
   */
  @Modifying
  @Transactional
  @Query(
      "UPDATE Utente u SET "
          + "u.nome = COALESCE(:#{#attributes['nome']}, u.nome), "
          + "u.cognome = COALESCE(:#{#attributes['cognome']}, u.cognome), "
          + "u.sesso = COALESCE(:#{#attributes['sesso']}, u.sesso), "
          + "u.dataDiNascita = COALESCE(:#{#attributes['dataDiNascita']}, u.dataDiNascita), "
          + "u.email = COALESCE(:#{#attributes['email']}, u.email), "
          + "u.telefono = COALESCE(:#{#attributes['telefono']}, u.telefono), "
          + "u.metaId = COALESCE(:#{#attributes['metaId']}, u.metaId), "
          + "u.tokenAuth = COALESCE(:#{#attributes['tokenAuth']}, u.tokenAuth) "
          + "WHERE u.metaId = :sessionId")
  int updateAttributes(
      @Param("sessionId") String sessionId, @Param("attributes") Map<String, Object> attributes);
}
