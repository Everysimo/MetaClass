package com.commigo.metaclass.MetaClass.gestionestanza.repository;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("StatoPartecipazioneRepository")
public interface StatoPartecipazioneRepository extends JpaRepository<StatoPartecipazione, Long> {
    StatoPartecipazione findStatoPartecipazioneByUtenteAndStanza(Utente utente, Stanza stanza);
    List<StatoPartecipazione> findAllByUtente(Utente utente);
}
