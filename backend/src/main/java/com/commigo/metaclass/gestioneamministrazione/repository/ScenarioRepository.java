package com.commigo.metaclass.gestioneamministrazione.repository;

import com.commigo.metaclass.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository dello scenario per gestire transazioni con i dati persistenti. */
@Repository("ScenarioRepository")
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

  /**
   * Metodo che permette la ricerca di uno scenario in base a un nome.
   *
   * @param nome Nome sul quale si basa la ricerca
   * @return Scenario dato un nome.
   */
  Scenario findByNome(String nome);

  /**
   * Metodo che permette la ricerca di uno scenario in base a un ID.
   *
   * @param id Id sul quale si basa la ricerca
   * @return Scenario dato un id.
   */
  Scenario findScenarioById(Long id);
}
