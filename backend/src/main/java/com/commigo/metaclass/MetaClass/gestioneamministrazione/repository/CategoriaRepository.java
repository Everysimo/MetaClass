package com.commigo.metaclass.MetaClass.gestioneamministrazione.repository;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CategoriaRepository")
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

  /**
   * Metodo che permette la ricerca di una categoria in base ad un nome
   *
   * @param nome Nome su cui si basa la ricerca della categoria
   * @return
   */
  Categoria findByNome(String nome);

  /**
   * metodo che permette la ricerca di una categoria in base ad un ID
   *
   * @param Id Id sul quale si basa la ricerca
   * @return
   */
  Categoria findById(long Id);
}
