package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.*;
import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.Validator;
import com.commigo.metaclass.MetaClass.utility.response.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("GestioneStanzaService")
@RequiredArgsConstructor
@Transactional    //ogni operazione è una transazione
public class GestioneStanzaServiceImpl implements GestioneStanzaService {

    private final StatoPartecipazioneRepository statoPartecipazioneRepository;
    private final RuoloRepository ruoloRepository;
    private final StanzaRepository stanzaRepository;
    private final UtenteRepository utenteRepository;

    @Override
    public ResponseEntity<ResponseBoolMessage> accessoStanza(String codiceStanza, String id_utente) {

        Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);
        if (stanza == null) {
            return ResponseEntity.ok(new ResponseBoolMessage(false, "Stanza non trovata"));
        } else if (!stanza.isTipo_Accesso()) {

            ResponseBoolMessage richiesta = richiestaAccessoStanza(codiceStanza, id_utente).getBody();
            return ResponseEntity.ok(richiesta);

        } else {

            try {
                Utente u = utenteRepository.findFirstByMetaId(id_utente);
                StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);

                if (statoPartecipazione == null) {
                    statoPartecipazione = new StatoPartecipazione(stanza, u, getRuolo(Ruolo.PARTECIPANTE), false, false, u.getNome());
                    return ResponseEntity.ok(new ResponseBoolMessage(true, "Stai per effettuare l'accesso alla stanza"));


                } else if (statoPartecipazione.isBannato()) {
                    return ResponseEntity.ok(new ResponseBoolMessage(false, "Sei stato bannato da questa stanza, non puoi entrare"));
                } else {
                    return ResponseEntity.ok(new ResponseBoolMessage(true, "Sei già all'interno di questa stanza"));
                }

            } catch (RuntimeException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseBoolMessage(false, "Errore durante la richiesta: " + e.getMessage()));
            }
        }
    }

    @Override
    public Stanza creaStanza(String nome, String Codice_Stanza, String Descrizione, boolean Tipo_Accesso, int MAX_Posti)
    {
        Stanza stanza = null;
        boolean isValid = Validator.isValid(nome) && Validator.isValid(Codice_Stanza) && Validator.isValid(Descrizione)
                && Validator.isValid(MAX_Posti);
        if(!isValid) return stanza;

        stanza = new Stanza(nome, Codice_Stanza, Descrizione, Tipo_Accesso, MAX_Posti);
        stanzaRepository.save(stanza);
        return stanza;
    }

    @Override
    public ResponseEntity<ResponseBoolMessage> richiestaAccessoStanza(String codiceStanza, String id_utente) {
        try {
            Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);

            Utente u = utenteRepository.findFirstByMetaId(id_utente);
            StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);

            if (statoPartecipazione == null) {
                statoPartecipazione = new StatoPartecipazione(stanza, u, getRuolo(Ruolo.PARTECIPANTE), true, false, u.getNome());
                return ResponseEntity.ok(new ResponseBoolMessage(true, "La stanza è privata, sei in attesa di entrare"));

            } else if (statoPartecipazione.isBannato()) {
                return ResponseEntity.ok(new ResponseBoolMessage(false, "Sei stato bannato da questa stanza, non puoi entrare"));

            } else if (statoPartecipazione.isInAttesa()) {
                return ResponseEntity.ok(new ResponseBoolMessage(true, "Sei già in attesa di entrare in questa stanza"));

            } else {
                return ResponseEntity.ok(new ResponseBoolMessage(true, "Sei già all'interno di questa stanza"));
            }


        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseBoolMessage(false, "Errore durante la richiesta: " + e.getMessage()));
        }

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
                return new Response<Boolean>(true, "L'utente selezionato ora è un organizzatore");

            }else if (stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)){
                return new Response<Boolean>(false, "L'utente selezionato è già un'organizzatore");

            }else{
                return new Response<Boolean>(false, "L'utente selezionato non può essere declassato ad organizzatore");
            }
        }else{
            return new Response<Boolean>(false, "Non puoi promuovere un'utente perché non sei un'organizzatore master");
        }
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
                return new Response<Boolean>(true, "L'utente selezionato ora è un partecipante");

            }else if (stato_og.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)){
                return new Response<Boolean>(false, "L'utente selezionato è già un partecipante");

            }else{
                return new Response<Boolean>(false, "L'utente selezionato non può essere declassato");
            }
        }else{
            return new Response<Boolean>(false, "Non puoi declassare un'utente perché non sei un'organizzatore master");
        }
    }

    @Override
    public Response<Boolean> deleteRoom(String id_Uogm, Long id_stanza){

        Utente ogm = utenteRepository.findFirstByMetaId(id_Uogm);
        Stanza stanza = stanzaRepository.findStanzaById(id_stanza);
        if(stanza == null) {
            return new Response<Boolean>(false, "La stanza selezionata non esiste");
        }

        StatoPartecipazione stato_ogm = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
        if(stato_ogm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)){
            stanzaRepository.delete(stanza);
            return new Response<Boolean>(true, "Stanza eliminata con successo");
        }else{
            return new Response<Boolean>(false, "Non puoi eliminare una stanza se non sei un'organizzatore master");
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
                    return new Response<Boolean>(false, "La stanza non esiste");
                }else{
                    if(stanzaRepository.updateAttributes(Id, dataMap)>0){
                        stanza = stanzaRepository.findStanzaById(Id);
                        return new Response<Boolean>(true, "modifica effettuata con successo");
                    }else{
                        stanza = existingStanza;
                        return new Response<Boolean>(true, "nessuna modifica effettuata");
                    }
                }
            }else{
                return new Response<Boolean>(false, "Non puoi effettuare modifiche sulla stanza se non sei almeno un organizzatore");
            }

        }catch (Exception e) {
            e.printStackTrace(); // Stampa la traccia dell'eccezione per debugging
            return new Response<Boolean>(false, "errore nella modifica dei dati");
        }
    }

    public Ruolo getRuolo(String nome){

        Ruolo ruolo = ruoloRepository.findByNome(nome);

        if(ruolo == null){
            ruolo = new Ruolo(nome);
            System.out.println(ruolo.toString());
            ruoloRepository.save(ruolo);
        }
        return ruolo;
    }
}
