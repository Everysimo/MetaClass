package com.commigo.metaclass.gestioneamministrazione.repository;

import com.commigo.metaclass.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository della categoria per gestire transazioni con i dati persistenti. */
@Repository("CategoriaRepository")
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

  /**
   * Metodo che permette la ricerca di una categoria in base a un nome.
   *
   * @param nome Nome su cui si basa la ricerca della categoria
   * @return Categoria associata a un nome.
   */
  Categoria findByNome(String nome);

  /**
   * Metodo che permette la ricerca di una categoria in base a un ID.
   *
   * @param id Id sul quale si basa la ricerca
   * @return Categoria associata a un Id.
   */
  Categoria findById(long id);
}
