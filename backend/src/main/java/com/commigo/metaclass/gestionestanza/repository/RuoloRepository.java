package com.commigo.metaclass.gestionestanza.repository;

import com.commigo.metaclass.entity.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("RuoloRepository")
public interface RuoloRepository extends JpaRepository<Ruolo, Long> {

  /**
   * metodo che permette di ricercare un ruolo sulla base di un nome
   *
   * @param nome nome su cui si bassa la ricerca
   * @return ruolo con il nome inserito
   */
  Ruolo findByNome(String nome);
}
