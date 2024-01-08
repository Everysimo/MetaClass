package com.commigo.metaclass.MetaClass.gestioneamministrazione.repository;

import com.commigo.metaclass.MetaClass.entity.Immagine;
import com.commigo.metaclass.MetaClass.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ImmagineRepository")
public interface ImmagineRepository extends JpaRepository<Immagine, Long> {

}
