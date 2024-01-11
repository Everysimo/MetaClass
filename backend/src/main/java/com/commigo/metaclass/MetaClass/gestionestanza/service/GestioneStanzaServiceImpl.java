package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException401;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.AccessResponse;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ValidationToken validationToken;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public ResponseEntity<AccessResponse<Integer>> accessoStanza(String codiceStanza, String id_utente) {

        Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);
        if (stanza == null) {
            return ResponseEntity.status(403).body(new AccessResponse<>(404, "La stanza non esiste", false));
        } else if (!stanza.isTipo_Accesso()) {

            AccessResponse<Integer> richiesta = richiestaAccessoStanza(codiceStanza, id_utente).getBody();
            if (richiesta.getValue() == 1 || richiesta.getValue() == 5) {
                return ResponseEntity.ok(new AccessResponse<>(richiesta.getValue(), richiesta.getMessage(), richiesta.isInAttesa()));
            } else {
                return ResponseEntity.status(403).body(new AccessResponse<>(richiesta.getValue(), richiesta.getMessage(), richiesta.isInAttesa()));
            }

        } else {

            try {
                Utente u = utenteRepository.findFirstByMetaId(id_utente);
                StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);

                if (statoPartecipazione == null) {

                    statoPartecipazione = new StatoPartecipazione(stanza, u, getRuolo(Ruolo.PARTECIPANTE), false, false, u.getNome());
                    return ResponseEntity.ok(new AccessResponse<>(1, "Stai per effettuare l'accesso alla stanza", false));

                } else if (statoPartecipazione.isBannato()) {

                    return ResponseEntity.status(403).body(new AccessResponse<>(2, "Sei stato bannato da questa stanza, non puoi entrare", false));

                } else {

                    return ResponseEntity.status(403).body(new AccessResponse<>(3, "Sei già all'interno di questa stanza", false));

                }

            } catch (RuntimeException e) {

                return ResponseEntity.status(403).body(new AccessResponse<>(4, "Errore durante la richiesta: " + e.getMessage(), false));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean creaStanza(Stanza s) throws Exception {
        String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

        if (metaID == null)
            throw new ServerRuntimeException("Errore col metaID");

        Utente u = utenteRepository.findFirstByMetaId(metaID);
        if (u == null)
            throw new ServerRuntimeException("Utente non trovato");

        //settaggio scenario
        Scenario sc = scenarioRepository.findScenarioById(s.getScenario().getId());
        if (sc != null)
            s.setScenario(sc);
        else
            throw new RuntimeException403("Scenario non trovato");

        stanzaRepository.save(s);

        //Prelevo l'id della stanza a cui si deve generare il codice
        Long id_stanza = stanzaRepository.findIdUltimaTupla();
        //Converto l'id in una stringa di 6 caratteri
        String codice = String.format("%06d", id_stanza);
        s.setCodice(codice);
        stanzaRepository.save(s);

       StatoPartecipazione sp = new StatoPartecipazione(s, u,
              getRuolo(Ruolo.ORGANIZZATORE_MASTER), false, false, u.getNome());

       statoPartecipazioneRepository.save(sp);

        return true;
    }


    @Override
    public Response<Boolean> downgradeUtente(String id_Uogm, long id_og, long id_stanza) {

        Utente ogm = utenteRepository.findFirstByMetaId(id_Uogm);
        Utente og = utenteRepository.findUtenteById(id_og);
        Stanza stanza = stanzaRepository.findStanzaById(id_stanza);


        StatoPartecipazione stato_ogm = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
        if (stato_ogm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)) {
            StatoPartecipazione stato_og = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
            if (stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)) {
                stato_og.getRuolo().setNome(Ruolo.PARTECIPANTE);

                return ResponseEntity.ok(new Response<>(true, "L'utente selezionato ora è un partecipante")).getBody();

            } else if (stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)) {

                return ResponseEntity.status(403).body(new Response<>(false, "L'utente selezionato è già un partecipante")).getBody();

            } else {
                return ResponseEntity.status(403).body(new Response<>(false, "L'utente selezionato non può essere declassato")).getBody();
            }
        } else {
            return ResponseEntity.status(403).body(new Response<>(false, "Non puoi declassare un'utente perché non sei un'organizzatore master")).getBody();
        }
    }

    @Override
    public Response<Boolean> deleteRoom(String metaID, Long id_stanza) {

        Utente ogm = utenteRepository.findFirstByMetaId(metaID);
        Stanza stanza = stanzaRepository.findStanzaById(id_stanza);
        if (stanza == null) {
            return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR, "La stanza selezionata non esiste").getBody();
        }

        StatoPartecipazione stato_ogm = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
        if (stato_ogm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER) || ogm.isAdmin()) {
            stanzaRepository.delete(stanza);
            return ResponseEntity.ok(new Response<>(true, "Stanza eliminata con successo")).getBody();
        } else {
            return ResponseEntity.status(403).body(new Response<>(false, "Non puoi eliminare una stanza se non sei un'organizzatore master")).getBody();
        }
    }

    @Override
    public Boolean modificaDatiStanza(Map<String, Object> params, Long id) throws RuntimeException403, RuntimeException401 {

        //controllo del ruolo di ogm
        String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());
        Utente ogm = utenteRepository.findFirstByMetaId(metaID);
        Stanza existingStanza = stanzaRepository.findStanzaById(id);

        if (existingStanza == null) {
            throw new RuntimeException403("La stanza non esiste");
        }

        StatoPartecipazione statoutente = statoPartecipazioneRepository.
                findStatoPartecipazioneByUtenteAndStanza(ogm, existingStanza);

        if (statoutente == null) throw new RuntimeException403("Non hai acceduto alla stanza");

        if (!statoutente.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)) {
            return stanzaRepository.updateAttributes(id, params) > 0;
        } else {
            throw new RuntimeException401("devi essere almeno un organizzatore");
        }
    }

    @Override
    public Stanza findStanza(Long id) {
        return stanzaRepository.findStanzaById(id);
    }


    @Override
    public ResponseEntity<AccessResponse<Integer>> richiestaAccessoStanza(String codiceStanza, String id_utente) {
        try {
            Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);

            Utente u = utenteRepository.findFirstByMetaId(id_utente);
            StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);

            if (statoPartecipazione == null) {

               // statoPartecipazione = new StatoPartecipazione(stanza, u, getRuolo(Ruolo.PARTECIPANTE), true, false, u.getNome());
                return ResponseEntity.ok(new AccessResponse<>(5, "La stanza è privata, sei in attesa di entrare", true));

            } else if (statoPartecipazione.isBannato()) {

                return ResponseEntity.status(403).body(new AccessResponse<>(6, "Sei stato bannato da questa stanza, non richiedere di entrare", false));

            } else if (statoPartecipazione.isInAttesa()) {

                return ResponseEntity.status(403).body(new AccessResponse<>(7, "Sei già in attesa di entrare in questa stanza", true));

            } else {

                return ResponseEntity.status(403).body(new AccessResponse<>(8, "Sei già all'interno di questa stanza", false));

            }


        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(new AccessResponse<>(0,
                    "Errore durante la richiesta: " + e.getMessage(), false));
        }

    }

    @Override
    public Response<Boolean> upgradeUtente(String id_Uogm, long id_og, long id_stanza) {

        Utente ogm = utenteRepository.findFirstByMetaId(id_Uogm);
        Utente og = utenteRepository.findUtenteById(id_og);
        Stanza stanza = stanzaRepository.findStanzaById(id_stanza);


        StatoPartecipazione stato_ogm = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
        if (stato_ogm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)) {
            StatoPartecipazione stato_og = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
            if (stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)) {
                stato_og.getRuolo().setNome(Ruolo.ORGANIZZATORE);
                statoPartecipazioneRepository.save(stato_og);

                return ResponseEntity.ok(new Response<>(true, "L'utente selezionato ora è un organizzatore")).getBody();

            } else if (stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)) {

                return ResponseEntity.status(403).body(new Response<>(false, "L'utente selezionato è già un'organizzatore")).getBody();

            } else {
                return ResponseEntity.status(403).body(new Response<>(false, "L'utente selezionato non può essere declassato ad organizzatore")).getBody();
            }
        } else {
            return ResponseEntity.status(403).body(new Response<>(false, "Non puoi promuovere un'utente perché non sei un'organizzatore master")).getBody();
        }
    }


    @Override
    public ResponseEntity<Response<List<Utente>>> visualizzaUtentiInStanza(Long Id) {

        Stanza stanza = stanzaRepository.findStanzaById(Id);

        if (stanza != null) {
            List<Utente> utenti = statoPartecipazioneRepository.findUtentiInStanza(Id);
            if (utenti != null) {
                return ResponseEntity.ok(new Response<>
                        (utenti, "operazione effettuata con successo"));
            } else {
                return ResponseEntity.ok(new Response<>(null, "Non sono presenti utenti all'interno della stanza"));
            }
        } else {
            return ResponseEntity.status(403).body(new Response<>(null, "La stanza selezionata non esiste"));
        }
    }

    @Override
    public ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(Long Id) {

        Stanza stanza = stanzaRepository.findStanzaById(Id);

        if (stanza != null) {
            List<Utente> utenti = statoPartecipazioneRepository.findUtentiBannatiInStanza(Id);
            if (utenti != null) {
                return ResponseEntity.ok(new Response<>
                        (utenti, "operazione effettuata con successo"));
            } else {
                return ResponseEntity.ok(new Response<>(null, "Non sono presenti utenti bannati all'interno della stanza"));
            }
        } else {
            return ResponseEntity.status(403).body(new Response<>(null, "La stanza selezionata non esiste"));
        }
    }

    @Override
    public ResponseEntity<Response<List<Utente>>> visualizzaUtentiInAttesaInStanza(Long Id, String metaID) {
        Stanza stanza = stanzaRepository.findStanzaById(Id);
        Utente u = utenteRepository.findFirstByMetaId(metaID);
        if (stanza != null) {
            StatoPartecipazione stato = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);
            if (stato != null) {
                if (stato.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER) || stato.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE) && !stato.isBannato()) {
                    List<Utente> utenti = statoPartecipazioneRepository.findUtentiInAttesaInStanza(Id);

                    if (utenti != null) {
                        return ResponseEntity.ok(new Response<>
                                (utenti, "operazione effettuata con successo"));
                    } else {
                        return ResponseEntity.ok(new Response<>(null, "Non sono presenti utenti in attesa all'interno della stanza"));
                    }
                } else {
                    return ResponseEntity.status(403).body(new Response<>(null, "Non puoi visualizzare gli utenti in attesa se non sei almeno un organizzatore"));
                }
            } else {
                return ResponseEntity.status(403).body(new Response<>(null, "Non puoi visualizzare gli utenti in attesa della stanza se non sei almeno un'organizzatore di essa"));
            }
        } else {
            return ResponseEntity.status(403).body(new Response<>(null, "La stanza selezionata non esiste"));
        }
    }

    @Override
    public Scenario visualizzaScenarioStanza(Stanza stanza) {
        return stanza.getScenario();
    }

    @Override
    public ResponseEntity<Response<Boolean>> modificaScenario(String metaID, Long idScenario, Long idStanza) {
        Utente u = utenteRepository.findFirstByMetaId(metaID);
        Stanza stanza = stanzaRepository.findStanzaById(idStanza);

        if (stanza != null) {
            StatoPartecipazione stato = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);
            if (stato != null) {
                if (stato.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER) || stato.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE) && !stato.isBannato()) {
                    Scenario scenario = scenarioRepository.findScenarioById(idScenario);
                    if (scenario != null) {
                        if (scenario != stanza.getScenario()) {
                            stanza.setScenario(scenario);
                            stanzaRepository.save(stanza);
                            return ResponseEntity.ok(new Response<>(true, "Lo scenario è stato modificato"));
                        } else {
                            return ResponseEntity.status(403).body(new Response<>(false, "Lo scenario selezionato è già in uso per la stanza"));
                        }
                    } else {
                        return ResponseEntity.status(403).body(new Response<>(false, "Lo scenario selezionato non esiste"));
                    }
                } else {
                    return ResponseEntity.status(403).body(new Response<>(false, "Non puoi modificare lo scenario se non sei almeno un organizzatore"));
                }
            } else {
                return ResponseEntity.status(403).body(new Response<>(false, "Non sei all'interno della stanza, non puoi modificare lo scenario"));
            }

        } else {
            return ResponseEntity.status(403).body(new Response<>(false, "La stanza selezionata non esiste"));
        }

    }

    public ResponseEntity<Response<Boolean>> modificaNomePartecipante(String metaID, Long idStanza, Long idUtente, String nome){
        Utente og = utenteRepository.findFirstByMetaId(metaID);
        Stanza stanza = stanzaRepository.findStanzaById(idStanza);

        Utente modifica = utenteRepository.findUtenteById(idUtente);
        if (stanza != null) {
            StatoPartecipazione statoOg = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
            if (statoOg != null) {
                if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER) || statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE) && !statoOg.isBannato()) {

                    StatoPartecipazione statoModifica = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(modifica, stanza);
                    if (statoModifica != null) {
                        statoModifica.setNomeInStanza(nome);
                        statoPartecipazioneRepository.save(statoModifica);

                        return ResponseEntity.ok(new Response<>(true, "Il nome in stanza è stato modificato"));
                    } else {
                        return ResponseEntity.status(403).body(new Response<>(false, "Il partecipante selezionato non è presente nella stanza"));
                    }
                } else {
                    return ResponseEntity.status(403).body(new Response<>(false, "Non puoi modificare il nome in stanza di un utente se non sei almeno un'organizzatore"));
                }
            } else {
                return ResponseEntity.status(403).body(new Response<>(false, "Non puoi modificare il nome in stanza dell'utente selezionato perché non è presente in stanza"));
            }
        }else{
            return ResponseEntity.status(403).body(new Response<>(false, "La stanza selezionata non esiste"));
        }
    }

    @Override
    public ResponseEntity<Response<Boolean>> kickPartecipante(String metaID, Long idStanza, Long idUtente){
        Utente og = utenteRepository.findFirstByMetaId(metaID);
        Stanza stanza = stanzaRepository.findStanzaById(idStanza);

        Utente kick = utenteRepository.findUtenteById(idUtente);
            if (stanza != null) {
            StatoPartecipazione statoOg = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
            if (statoOg != null) {
                if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER) || statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE) && !statoOg.isBannato()) {

                    StatoPartecipazione statoKick = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(kick, stanza);
                    if (statoKick != null) {
                        statoPartecipazioneRepository.delete(statoKick);
                        return ResponseEntity.ok(new Response<>(true, "L'utente è stato kickato con successo"));
                    } else {
                        return ResponseEntity.status(403).body(new Response<>(false, "Il partecipante selezionato non è presente nella stanza"));
                    }
                } else {
                    return ResponseEntity.status(403).body(new Response<>(false, "Non puoi kickare un utente se non sei almeno un'organizzatore"));
                }
            } else {
                return ResponseEntity.status(403).body(new Response<>(false, "Il partecipante selezionato non è presente nella stanza"));
            }
        }else{
            return ResponseEntity.status(403).body(new Response<>(false, "La stanza selezionata non esiste"));
        }
    }

/**
*
 * @param metaID
 * @param idStanza
 * @return
*/
    @Override
    public Ruolo getRuoloByUserAndStanzaID(String metaID, Long idStanza) throws ServerRuntimeException, RuntimeException403 {

        Utente u;
        Stanza stanza;
        if((u= utenteRepository.findFirstByMetaId(metaID))==null)
            throw new ServerRuntimeException(("Utente non torvato"));
        if((stanza = stanzaRepository.findStanzaById(idStanza))==null)
            throw new RuntimeException403("Stanza non trovata");

        StatoPartecipazione sp;
        if((sp=statoPartecipazioneRepository
                .findStatoPartecipazioneByUtenteAndStanza(u,stanza))==null)
            throw new RuntimeException403("L'utente non ha acceduto alla stanza");

        if(sp.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)){
           if(sp.isBannato())   throw new RuntimeException403("Utente bannato dalla stanza");
           if(sp.isInAttesa())   throw new RuntimeException403("Utente in attesa di entrare in stanza");
        }

        return sp.getRuolo();
    }

    @Override
    public ResponseEntity<Response<Boolean>> gestioneAccesso(String metaID, Long idUtente, Long idStanza, boolean scelta) {

        Utente og = utenteRepository.findFirstByMetaId(metaID);
        Utente accesso = utenteRepository.findUtenteById(idUtente);
        Stanza stanza = stanzaRepository.findStanzaById(idStanza);

        if(stanza != null) {
            StatoPartecipazione statoOg = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
            if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER) || statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE) && !statoOg.isBannato()) {
                StatoPartecipazione statoAccesso = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(accesso, stanza);
                if (statoAccesso.isInAttesa()) {
                    if (scelta) {
                        statoAccesso.setInAttesa(false);
                        statoPartecipazioneRepository.save(statoAccesso);
                        return ResponseEntity.ok(new Response<>(true, "L'utente selezionato non è più in attesa e sta per entrare nella stanza"));
                    } else {
                        statoPartecipazioneRepository.delete(statoAccesso);
                        return ResponseEntity.ok(new Response<>(true, "L'utente selezionato non è più in attesa, hai rifiutato la richiesta di accesso alla stanza"));
                    }
                } else {
                    return ResponseEntity.status(403).body(new Response<>(false, "L'utente selezioanto non è in attesa di entrare in questa stanza"));
                }
            }else{
                return ResponseEntity.status(403).body(new Response<>(false, "Per accettare o rifiutare richiesta di accesso alla stanza devi essere almeno un organizzatore"));
            }
        }else{
            return ResponseEntity.status(403).body(new Response<>(false, "La stanza selezionata non esiste"));
        }
    }

    /**
             * @param Id
             * @return
             */
    @Override
    public Stanza visualizzaStanza(Long Id) {
        return stanzaRepository.findStanzaById(Id);
    }

    public Ruolo getRuolo(String nome) {

        Ruolo ruolo = ruoloRepository.findByNome(nome);

        if (ruolo == null) {
            ruolo = new Ruolo(nome);
            System.out.println(ruolo);
            ruoloRepository.save(ruolo);
        }
        return ruolo;
    }

    public List<Scenario> getAllScenari() {
        return scenarioRepository.findAll();
    }
}
