package com.commigo.metaclass.MetaClass.gestionestanza.repository;

import com.commigo.metaclass.MetaClass.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UtenteRepositoryStanza")
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Utente findById(long id);

}
