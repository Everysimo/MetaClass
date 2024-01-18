package com.commigo.metaclass.MetaClass.gestioneamministrazione.repository;

import com.commigo.metaclass.MetaClass.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ScenarioRepository")
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

     /**
      * Metodo che permette la ricerca di uno scenario in base ad un nome
      * @param nome Nome sul quale si basa la ricerca
      * @return
      */
     Scenario findByNome(String nome);

     /**
      * Metodo che permette la ricerca di uno scenario in base ad un ID
      * @param id Id sul quale si basa la ricerca
      * @return
      */
     Scenario findScenarioById(Long id);
}
