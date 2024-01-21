package com.commigo.metaclass.gestioneamministrazione.repository;

import com.commigo.metaclass.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CategoriaRepository")
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

  /**
   * Metodo che permette la ricerca di una categoria in base ad un nome.
   *
   * @param nome Nome su cui si basa la ricerca della categoria
   * @return
   */
  Categoria findByNome(String nome);

  /**
   * metodo che permette la ricerca di una categoria in base ad un ID.
   *
   * @param id Id sul quale si basa la ricerca
   * @return
   */
  Categoria findById(long id);
}
