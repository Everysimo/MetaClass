package com.commigo.metaclass.MetaClass.gestioneamministrazione.repository;

import com.commigo.metaclass.MetaClass.entity.Immagine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ImmagineRepository")
public interface ImmagineRepository extends JpaRepository<Immagine, Long> {

    Immagine findImmagineById(long id);
    Immagine findByNome(String nome);
}
