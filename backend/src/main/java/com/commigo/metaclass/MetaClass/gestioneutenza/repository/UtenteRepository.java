package com.commigo.metaclass.MetaClass.gestioneutenza.repository;

import com.commigo.metaclass.MetaClass.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UtenteRepository")
public interface UtenteRepository extends JpaRepository<Utente, Long> {

}
