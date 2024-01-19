package com.commigo.metaclass.MetaClass.gestioneamministrazione.service;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;

import java.util.List;

public interface GestioneAmministrazioneService {

    boolean updateCategoria(Categoria c);
    boolean updateScenario(Scenario s, long IdCategoria) throws ServerRuntimeException;
    List<Stanza> getStanze();
    boolean deleteBanToUser(Long idUtente, Long idStanza) throws RuntimeException403;

}
