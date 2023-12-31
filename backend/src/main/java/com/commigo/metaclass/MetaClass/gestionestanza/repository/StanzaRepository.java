package com.commigo.metaclass.MetaClass.gestionestanza.repository;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("StanzaRepository")
public interface StanzaRepository extends JpaRepository<Stanza, Long> {

    //Stanza findByCodice_Stanza(String codice);

}
