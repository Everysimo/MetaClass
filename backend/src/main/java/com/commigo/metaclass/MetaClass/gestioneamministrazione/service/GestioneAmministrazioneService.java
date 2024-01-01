package com.commigo.metaclass.MetaClass.gestioneamministrazione.service;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import com.commigo.metaclass.MetaClass.entity.Scenario;

public interface GestioneAmministrazioneService {

    boolean updateCategoria(Categoria c);
    boolean updateScenario(Scenario s, long IdCategoria);
}
