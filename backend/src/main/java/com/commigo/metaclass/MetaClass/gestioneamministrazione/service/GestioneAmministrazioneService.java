package com.commigo.metaclass.MetaClass.gestioneamministrazione.service;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;

import java.util.List;

public interface GestioneAmministrazioneService {

    Utente findUtenteById(String id);
    Stanza findStanzaById(Long id);
    boolean isBannedUser(Utente utente,Stanza stanza);
    boolean updateCategoria(Categoria c);
    boolean updateScenario(Scenario s, long IdCategoria);
    List<Stanza> getStanze();
}
