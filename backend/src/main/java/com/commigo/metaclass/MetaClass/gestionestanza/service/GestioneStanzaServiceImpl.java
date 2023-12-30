package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("GestioneStanzaService")
@RequiredArgsConstructor
@Transactional    //ogni operazione è una transazione
public class GestioneStanzaServiceImpl implements GestioneStanzaService{

    private final StanzaRepository stanzaRepository;
    private final StatoPartecipazioneRepository statoPartecipazioneRepository;
    private final UtenteRepository utenteRepository;
    private final RuoloRepository ruoloRepository;
    @Override
    public boolean richiestaAccessoStanza(String codiceStanza, long id_utente) {

        Stanza stanza = stanzaRepository.findByCodice_Stanza(codiceStanza);
        if(stanza == null){
            return false;
        }else if(!stanza.isTipo_Accesso()){
            StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findByUtenteAndStanza(id_utente, stanza.getId());
            if(statoPartecipazione == null){
                Utente u = utenteRepository.findById(id_utente);
                //Dobbiamo cambiare i costruttori delle Entity altrimenti non va
                //Dobbiamo capire come impostare soltanto 4 ruoli e non crearne sempre di nuovi
                //statoPartecipazione = new StatoPartecipazione(stanza, u, ruoloRepository.findByNome("Partecipante"), true, false, u.getNome());
            } else if (!statoPartecipazione.isBannato()){
                statoPartecipazione.setInAttesa(true);
            }
            return true;
        }
        else
            return false;
    }
}
