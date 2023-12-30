package com.commigo.metaclass.MetaClass.gestionestanza.repository;

import com.commigo.metaclass.MetaClass.entity.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("RuoloRepository")
public interface RuoloRepository extends JpaRepository<Ruolo, Long> {
    Ruolo findByNome(String nome);

}
