package com.commigo.metaclass.MetaClass.gestioneamministrazione.service;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.CategoriaRepository;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("GestioneAmministrazioneService")
public class GestioneAmministrazioneServiceImpl implements GestioneAmministrazioneService{

    @Autowired
    @Qualifier("CategoriaRepository")
    private CategoriaRepository categoriaRepository;

    @Autowired
    @Qualifier("ScenarioRepository")
    private ScenarioRepository scenarioRepository;

    @Autowired
    @Qualifier("StanzaRepository")
    private StanzaRepository stanzaRepository;

    @Autowired
    @Qualifier("UtenteRepository")
    private UtenteRepository utenteRepository;

    @Autowired
    @Qualifier("StatoPartecipazioneRepository")
    private StatoPartecipazioneRepository statoPartecipazioneRepository;

    @Override
    public Utente findUtenteById(String id) {
        return utenteRepository.findUtenteById(utenteRepository.findByMetaId(id));
    }

    @Override
    public Stanza findStanzaById(Long id)
    {
        return stanzaRepository.findStanzaById(id);
    }

    @Override
    public boolean isBannedUser(Utente utente, Stanza stanza) {
        return statoPartecipazioneRepository.isBannedUser(stanza.getId(),utente.getId());
    }

    /**
     * @param c
     * @return
     */
    @Override
    public boolean updateCategoria(Categoria c) {
         if(categoriaRepository.findByNome(c.getNome())==null){
             categoriaRepository.save(c);
             return true;
         }
         return false;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public boolean updateScenario(Scenario s, long IdCategoria) {

        //gestione della categoria
        Categoria cat;
        if((cat = categoriaRepository.findById(IdCategoria))==null)   return false;
        s.setCategoria(cat);

        //getsione dello scenario
        if((scenarioRepository.findByNome(s.getNome()))==null){
            scenarioRepository.save(s);
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    @Override
    public List<Stanza> getStanze() {
        try{
            return stanzaRepository.findAll();
        }catch(Exception e){
            return null;
        }
    }
}

