package com.commigo.metaclass.MetaClass.gestioneamministrazione.repository;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CategoriaRepository")
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
     Categoria findByNome(String nome);
     Categoria findById(long Id);
}
