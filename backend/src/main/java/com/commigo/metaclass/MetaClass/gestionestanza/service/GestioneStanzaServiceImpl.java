package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.Validator;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.AccessResponse;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("GestioneStanzaService")
@RequiredArgsConstructor
@Transactional    //ogni operazione è una transazione
public class GestioneStanzaServiceImpl implements GestioneStanzaService {

    private final StatoPartecipazioneRepository statoPartecipazioneRepository;
    private final RuoloRepository ruoloRepository;
    private final StanzaRepository stanzaRepository;
    private final UtenteRepository utenteRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    public ResponseEntity<AccessResponse<Boolean>> accessoStanza(String codiceStanza, String id_utente) {

        Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);
        if (stanza == null) {
            return ResponseEntity.status(403).body(new AccessResponse<>(false, "La stanza non esiste", false));
        } else if (!stanza.isTipo_Accesso()) {

            AccessResponse<Boolean> richiesta = richiestaAccessoStanza(codiceStanza, id_utente).getBody();
            if(richiesta.getValue()){
                return ResponseEntity.ok(new AccessResponse<>(richiesta.getValue(), richiesta.getMessage(), richiesta.isInAttesa()));
            }else{
                return ResponseEntity.status(403).body(new AccessResponse<>(richiesta.getValue(), richiesta.getMessage(), richiesta.isInAttesa()));
            }

        } else {

            try {
                Utente u = utenteRepository.findFirstByMetaId(id_utente);
                StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);

                if (statoPartecipazione == null) {

                    statoPartecipazione = new StatoPartecipazione(stanza, u, getRuolo(Ruolo.PARTECIPANTE), false, false, u.getNome());
                    return ResponseEntity.ok(new AccessResponse<>(true, "Stai per effettuare l'accesso alla stanza", false));

                } else if (statoPartecipazione.isBannato()) {

                    return ResponseEntity.status(403).body(new AccessResponse<>(false, "Sei stato bannato da questa stanza, non puoi entrare", false));

                } else {

                    return ResponseEntity.status(403).body(new AccessResponse<>(false, "Sei già all'interno di questa stanza", false));

                }

            } catch (RuntimeException e) {

                return ResponseEntity.status(403).body(new AccessResponse<>(false, "Errore durante la richiesta: " + e.getMessage(), false));

            }
        }
    }

    @Override
    public boolean creaStanza(Stanza s)
    {
        Stanza stanza = stanzaRepository.findStanzaByCodice(s.getCodice());
        if(stanza == null){

             //settaggio scenario
             Scenario sc = scenarioRepository.findScenarioById(s.getScenario().getId());
             if(sc != null)
                  s.setScenario(sc);
             else return false;

             stanzaRepository.save(s);
             return true;
        }
        return false;
    }



    @Override
    public Response<Boolean> downgradeUtente(String id_Uogm, long id_og, long id_stanza){

        Utente ogm = utenteRepository.findFirstByMetaId(id_Uogm);
        Utente og = utenteRepository.findUtenteById(id_og);
        Stanza stanza = stanzaRepository.findStanzaById(id_stanza);


        StatoPartecipazione stato_ogm = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
        if(stato_ogm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)){
            StatoPartecipazione stato_og = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
            if(stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)){
                stato_og.getRuolo().setNome(Ruolo.PARTECIPANTE);

                return ResponseEntity.ok(new Response<>(true, "L'utente selezionato ora è un partecipante")).getBody();

            }else if (stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)){

                return ResponseEntity.status(403).body(new Response<>(false, "L'utente selezionato è già un partecipante")).getBody();

            }else{
                return ResponseEntity.status(403).body(new Response<>(false, "L'utente selezionato non può essere declassato")).getBody();
            }
        }else{
            return ResponseEntity.status(403).body(new Response<>(false, "Non puoi declassare un'utente perché non sei un'organizzatore master")).getBody();
        }
    }

    @Override
    public Response<Boolean> deleteRoom(String metaID, Long id_stanza){

        Utente ogm = utenteRepository.findFirstByMetaId(metaID);
        Stanza stanza = stanzaRepository.findStanzaById(id_stanza);
        if(stanza == null) {
            return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,"La stanza selezionata non esiste").getBody();
        }

        StatoPartecipazione stato_ogm = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
        if(stato_ogm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)){
            stanzaRepository.delete(stanza);
            return ResponseEntity.ok(new Response<>(true, "Stanza eliminata con successo")).getBody();
        }else{
            return ResponseEntity.status(403).body(new Response<>(false, "Non puoi eliminare una stanza se non sei un'organizzatore master")).getBody();
        }
    }

    @Override
    public Response<Boolean> modificaDatiStanza(String id, Long Id, Map<String, Object> dataMap, Stanza stanza) {

        try {
            Utente ogm = utenteRepository.findFirstByMetaId(id);
            stanza = stanzaRepository.findStanzaById(Id);

            StatoPartecipazione statoutente = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
            if(statoutente.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER) || statoutente.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)){

                Stanza existingStanza = stanzaRepository.findStanzaById(Id);
                if(existingStanza == null) {
                    return ResponseEntity.status(403).body(new Response<>(false, "La stanza non esiste")).getBody();
                }else{
                    if(stanzaRepository.updateAttributes(Id, dataMap)>0){
                        stanza = stanzaRepository.findStanzaById(Id);
                        return ResponseEntity.ok(new Response<>(true, "modifica effettuata con successo")).getBody();
                    }else{
                        stanza = existingStanza;
                        return ResponseEntity.ok(new Response<>(true, "nessuna modifica effettuata")).getBody();
                    }
                }
            }else{
                return ResponseEntity.status(403).body(new Response<>(false, "Non puoi effettuare modifiche sulla stanza se non sei almeno un organizzatore")).getBody();
            }

        }catch (Exception e) {
            return ResponseEntity.status(403).body(new Response<>(false, "errore nella modifica dei dati")).getBody();
        }
    }

    @Override
    public Stanza findStanza(Long id)
    {
        return stanzaRepository.findStanzaById(id);
    }

    @Override
    public List<StatoPartecipazione> findStatoPartecipazioniInAttesa(Stanza stanza,boolean isInAttesa)
    {
        return statoPartecipazioneRepository.finsAllByStanzaAndisInAttesa(stanza.getId(),isInAttesa);
    }

    @Override
    public ResponseEntity<AccessResponse<Boolean>> richiestaAccessoStanza(String codiceStanza, String id_utente) {
        try {
            Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);

            Utente u = utenteRepository.findFirstByMetaId(id_utente);
            StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);

            if (statoPartecipazione == null) {

                statoPartecipazione = new StatoPartecipazione(stanza, u, getRuolo(Ruolo.PARTECIPANTE), true, false, u.getNome());
                return ResponseEntity.ok(new AccessResponse<>(true, "La stanza è privata, sei in attesa di entrare", true));

            } else if (statoPartecipazione.isBannato()) {

                return ResponseEntity.status(403).body(new AccessResponse<>(false, "Sei stato bannato da questa stanza, non richiedere di entrare", false));

            } else if (statoPartecipazione.isInAttesa()) {

                return ResponseEntity.status(403).body(new AccessResponse<>(false, "Sei già in attesa di entrare in questa stanza", true));

            } else {

                return ResponseEntity.status(403).body(new AccessResponse<>(false, "Sei già all'interno di questa stanza", false));

            }


        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(new AccessResponse<>(false,
                    "Errore durante la richiesta: " + e.getMessage(), false));
        }

    }

    @Override
    public StatoPartecipazione setStatoPartecipazione(Stanza stanza, Utente utente, boolean isInAttesa)
    {
        StatoPartecipazione sp = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(utente,stanza);
        sp.setInAttesa(isInAttesa);
        statoPartecipazioneRepository.save(sp);
        return sp;
    }

    @Override
    public Response<Boolean> upgradeUtente(String id_Uogm, long id_og, long id_stanza){

        Utente ogm = utenteRepository.findFirstByMetaId(id_Uogm);
        Utente og = utenteRepository.findUtenteById(id_og);
        Stanza stanza = stanzaRepository.findStanzaById(id_stanza);


        StatoPartecipazione stato_ogm = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
        if(stato_ogm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)){
            StatoPartecipazione stato_og = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
            if(stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)){
                stato_og.getRuolo().setNome(Ruolo.ORGANIZZATORE);

                return ResponseEntity.ok(new Response<>(true, "L'utente selezionato ora è un organizzatore")).getBody();

            }else if (stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)){

                return ResponseEntity.status(403).body(new Response<>(false, "L'utente selezionato è già un'organizzatore")).getBody();

            }else{
                return ResponseEntity.status(403).body(new Response<>(false, "L'utente selezionato non può essere declassato ad organizzatore")).getBody();
            }
        }else{
            return ResponseEntity.status(403).body(new Response<>(false, "Non puoi promuovere un'utente perché non sei un'organizzatore master")).getBody();
        }
    }


    @Override
    public List<Utente> visualizzaStanza(Long Id) {
        return statoPartecipazioneRepository.findUtentiInStanza(Id);
    }

    public Ruolo getRuolo(String nome){

        Ruolo ruolo = ruoloRepository.findByNome(nome);

        if(ruolo == null){
            ruolo = new Ruolo(nome);
            System.out.println(ruolo);
            ruoloRepository.save(ruolo);
        }
        return ruolo;
    }

    public List<Scenario> getAllScenari(){
        return scenarioRepository.findAll();
    }
}
