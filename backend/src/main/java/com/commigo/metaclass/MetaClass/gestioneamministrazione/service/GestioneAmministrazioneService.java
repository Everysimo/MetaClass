package com.commigo.metaclass.MetaClass.gestioneamministrazione.service;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;

import java.util.List;

public interface GestioneAmministrazioneService {

    boolean updateCategoria(Categoria c);
    boolean updateScenario(Scenario s, long IdCategoria);
    List<Stanza> getStanze();
}
