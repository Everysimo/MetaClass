package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.*;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
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
    public boolean richiestaAccessoStanza(String codiceStanza, String id_utente) {

        Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);
        if (stanza == null) {
            System.out.println("NULL"+ "codiceStanza" + codiceStanza);
            return false;
        } else if (!stanza.isTipo_Accesso()) {
            Utente u = utenteRepository.findFirstByMetaId(id_utente);
            StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);
            if (statoPartecipazione == null) {
                statoPartecipazione = new StatoPartecipazione(stanza, u, getRuolo(Ruolo.PARTECIPANTE), true, false, u.getNome());
            } else if (!statoPartecipazione.isBannato()) {
                statoPartecipazione.setInAttesa(true);
            }
            return true;
        } else {
            System.out.println("NoNULL");
            return false;
        }
    }

    public Ruolo getRuolo(String nome){

        Ruolo ruolo = ruoloRepository.findByNome(nome);
        if(ruolo == null){
            ruolo = new Ruolo(nome);
            ruoloRepository.save(ruolo);
        }

        return ruolo;
    }

}
