package com.commigo.metaclass.MetaClass.repository;

import com.commigo.metaclass.MetaClass.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

}
