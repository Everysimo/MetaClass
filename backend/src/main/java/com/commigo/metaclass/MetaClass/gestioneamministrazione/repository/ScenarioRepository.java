package com.commigo.metaclass.MetaClass.gestioneamministrazione.repository;

import com.commigo.metaclass.MetaClass.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ScenarioRepository")
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
     Scenario findByNome(String nome);
}
