package com.commigo.metaclass.gestioneamministrazione.repository;

import com.commigo.metaclass.entity.Immagine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ImmagineRepository")
public interface ImmagineRepository extends JpaRepository<Immagine, Long> {}
