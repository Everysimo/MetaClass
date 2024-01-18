package com.commigo.metaclass.MetaClass.gestionestanza.repository;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("StatoPartecipazioneRepository")
public interface StatoPartecipazioneRepository extends JpaRepository<StatoPartecipazione, Long> {

    /**
     *
     * @param utente
     * @param stanza
     * @return
     */
    StatoPartecipazione findStatoPartecipazioneByUtenteAndStanza(Utente utente, Stanza stanza);

    /**
     *
     * @param utente
     * @return
     */
    List<StatoPartecipazione> findAllByUtente(Utente utente);

    /**
     *
     * @param id
     * @return
     */
    @Query("SELECT sp.utente FROM StatoPartecipazione sp WHERE sp.isInAttesa=false AND  sp.isBannato=false and sp.stanza.id = :id")
    List<Utente> findUtentiInStanza(@Param("id") Long id);

    /**
     *
     * @param id
     * @return
     */
    @Query("SELECT sp.utente FROM StatoPartecipazione sp WHERE sp.isInAttesa=false AND  sp.isBannato=true and sp.stanza.id = :id")
    List<Utente> findUtentiBannatiInStanza(@Param("id") Long id);

    /**
     *
     * @param id
     * @return
     */
    @Query("SELECT sp.utente FROM StatoPartecipazione sp WHERE sp.isInAttesa=true AND  sp.isBannato=false and sp.stanza.id = :id")
    List<Utente> findUtentiInAttesaInStanza(@Param("id") Long id);

}
