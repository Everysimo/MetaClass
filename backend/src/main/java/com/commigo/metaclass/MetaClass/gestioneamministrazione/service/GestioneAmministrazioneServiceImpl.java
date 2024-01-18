package com.commigo.metaclass.MetaClass.gestioneamministrazione.service;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
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


    /**
     * Metodo che permette la modifica di una Categoria
     * @param c Categoria che deve essere modificata
     * @return Valore booleno che identifica il successo dell'operazione
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
     * Metodo che permette la modifica di uno scenario
     * @param s Scenario che deve essere modificato
     * @return valore boolean che identifica il successo dell'operazione
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
     * Meotodo che permette di ottenere una lista di stanze
     * @return Una lista di stanze
     */
    @Override
    public List<Stanza> getStanze() {
        try{
            return stanzaRepository.findAll();
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Metodo che permette l'eliminazione di un ban di un determinato utente in una determinata stanza
     * @param idUtente Id dell'utente a cui deve essere eliminato il ban nella stanza
     * @param idStanza Id della stanza dalla quale va eliminato il ban dell'utente
     * @return valore boolean che identifica la riuscita dell'operazione
     * @throws RuntimeException403
     */
    @Override
    public boolean deleteBanToUser(Long idUtente, Long idStanza) throws RuntimeException403 {
        Utente u;
        Stanza s;

        if((u=utenteRepository.findUtenteById(idUtente))==null)
            throw new RuntimeException403("utente non trovato");

        if((s=stanzaRepository.findStanzaById(idStanza))==null)
            throw new RuntimeException403("stanza non trovata");

        StatoPartecipazione sp;
        if((sp=statoPartecipazioneRepository
                .findStatoPartecipazioneByUtenteAndStanza(u,s))==null)
            throw new RuntimeException403("L'utente non ha acceduto alla stanza "+s.getNome());

        if(sp.isInAttesa())
            throw new RuntimeException403("L'utente ancora viene accettato nella stanza "+s.getNome());

        if(!sp.isBannato())
            throw new RuntimeException403("L'utente non è bannato nella stanza "+s.getNome());

        sp.setBannato(false);
        statoPartecipazioneRepository.save(sp);
        return true;

    }
}

