package com.commigo.metaclass.MetaClass.gestionestanza.repository;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("StatoPartecipazioneRepository")
public interface StatoPartecipazioneRepository extends JpaRepository<StatoPartecipazione, Long> {
    StatoPartecipazione findStatoPartecipazioneByUtenteAndStanza(Utente utente, Stanza stanza);
    List<StatoPartecipazione> findAllByUtente(Utente utente);
    @Query("SELECT sp.utente FROM StatoPartecipazione sp WHERE sp.isInAttesa=false AND  sp.isBannato=false and sp.stanza.id = :id")
    List<Utente> findUtentiInStanza(@Param("id") Long id);
}
