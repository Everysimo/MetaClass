package com.commigo.metaclass.gestioneamministrazione.service;

import com.commigo.metaclass.entity.Categoria;
import com.commigo.metaclass.entity.Scenario;
import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.exceptions.RuntimeException403;

import java.util.List;

public interface GestioneAmministrazioneService {

  boolean updateCategoria(Categoria c);

  boolean updateScenario(Scenario s, long IdCategoria);

  List<Stanza> getStanze();

  boolean deleteBanToUser(Long idUtente, Long idStanza) throws RuntimeException403;

  boolean checkAdmin(String metaId);

  List<Categoria> getCategorie();
}
