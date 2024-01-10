package com.commigo.metaclass.MetaClass.gestioneamministrazione.service;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.CategoriaRepository;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ImmagineRepository;
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
    @Qualifier("ImmagineRepository")
    private ImmagineRepository immagineRepository;

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
    public boolean deleteBanToUser(Long idUtente, Long idStanza) throws RuntimeException403, ServerRuntimeException {

        Utente utente = utenteRepository.findUtenteById(idUtente);
        if(utente == null) throw new RuntimeException403("Utente non trovato");

        Stanza stanza = stanzaRepository.findStanzaById(idStanza);
        if(stanza == null) throw new RuntimeException403("Stanza non trovata");

        StatoPartecipazione sp = statoPartecipazioneRepository
                .findStatoPartecipazioneByUtenteAndStanza(utente, stanza);
        if(sp==null) throw new ServerRuntimeException("Stanza non trovata");

        if(!sp.isBannato())   throw new RuntimeException403("L'utente non e' bannato");

        sp.setBannato(false);
        statoPartecipazioneRepository.save(sp);

        return true;

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

        Immagine image = immagineRepository.save(new Immagine(s.getImage().getUrl()));
        s.setImage(image);

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

