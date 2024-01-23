package com.commigo.metaclass.gestioneamministrazione.repository;

import com.commigo.metaclass.entity.Immagine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository dell'immagine per gestire transazioni con i dati persistenti. */
@Repository("ImmagineRepository")
public interface ImmagineRepository extends JpaRepository<Immagine, Long> {}
