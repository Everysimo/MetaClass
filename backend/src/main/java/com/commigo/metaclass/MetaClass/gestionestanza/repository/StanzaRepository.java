package com.commigo.metaclass.MetaClass.gestionestanza.repository;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository("StanzaRepository")
public interface StanzaRepository extends JpaRepository<Stanza, Long> {

    Stanza findStanzaByCodice(String codice);
    Stanza findStanzaById(long id);

    @Query("SELECT s.id FROM Stanza s ORDER BY s.id DESC LIMIT 1")
    Long findIdUltimaTupla();

    //il valore restituito indica il numero di righe modificate, quindi:
    // 1 se tutto ok
    // 0 se non è ok
    @Modifying
    @Transactional
    @Query("UPDATE Stanza stanza SET " +
            "stanza.nome = COALESCE(:#{#attributes['nome']}, stanza.nome), " +
            "stanza.codice = COALESCE(:#{#attributes['codice']}, stanza.codice), " +
            "stanza.descrizione = COALESCE(:#{#attributes['descrizione']}, stanza.descrizione), " +
            "stanza.tipo_Accesso = COALESCE(:#{#attributes['tipo_Accesso']}, stanza.tipo_Accesso), " +
            "stanza.max_Posti = COALESCE(:#{#attributes['max_Posti']}, stanza.max_Posti), " +
            "stanza.scenario.id = COALESCE(:#{#attributes['scenario']['id']}, stanza.scenario.id) " +
            "WHERE stanza.id = :Id")
    int updateAttributes(@Param("Id") Long Id,
                         @Param("attributes") Stanza attributes);


    @Modifying
    @Transactional
    @Query("UPDATE Stanza stanza SET " +
            "stanza.nome = COALESCE(:#{#attributes['nome']}, stanza.nome), " +
            "stanza.codice = COALESCE(:#{#attributes['codice']}, stanza.codice), " +
            "stanza.descrizione = COALESCE(:#{#attributes['descrizione']}, stanza.descrizione), " +
            "stanza.tipo_Accesso = COALESCE(:#{#attributes['tipo_Accesso']}, stanza.tipo_Accesso), " +
            "stanza.max_Posti = COALESCE(:#{#attributes['max_Posti']}, stanza.max_Posti), " +
            "stanza.scenario.id = COALESCE(:#{#attributes['id_scenario']}, stanza.scenario.id) " +
            "WHERE stanza.id = :Id")
    int updateAttributes(@Param("Id") Long Id,
                         @Param("attributes") Map<String, Object> attributes);

    void deleteStanzaById(Long id);





}
