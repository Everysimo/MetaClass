package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.*;
import com.commigo.metaclass.MetaClass.gestionestanza.controller.RispostaRichiestaAccessoStanza;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.Validator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("GestioneStanzaService")
@RequiredArgsConstructor
@Transactional    //ogni operazione è una transazione
public class GestioneStanzaServiceImpl implements GestioneStanzaService {

    private final StatoPartecipazioneRepository statoPartecipazioneRepository;
    private final RuoloRepository ruoloRepository;
    private final StanzaRepository stanzaRepository;
    private final UtenteRepository utenteRepository;

    @Override
    public ResponseEntity<RispostaRichiestaAccessoStanza> accessoStanza(String codiceStanza, String id_utente) {

        Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);
        if (stanza == null) {
            return ResponseEntity.ok(new RispostaRichiestaAccessoStanza(false, "Stanza non trovata"));
        } else if (!stanza.isTipo_Accesso()) {

            RispostaRichiestaAccessoStanza richiesta = richiestaAccessoStanza(codiceStanza, id_utente).getBody();
            return ResponseEntity.ok(richiesta);

        } else {

            try {
                Utente u = utenteRepository.findFirstByMetaId(id_utente);
                StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);

                if (statoPartecipazione == null) {
                    statoPartecipazione = new StatoPartecipazione(stanza, u, getRuolo(Ruolo.PARTECIPANTE), false, false, u.getNome());
                    return ResponseEntity.ok(new RispostaRichiestaAccessoStanza(true, "Stai per effettuare l'accesso alla stanza"));


                } else if (statoPartecipazione.isBannato()) {
                    return ResponseEntity.ok(new RispostaRichiestaAccessoStanza(false, "Sei stato bannato da questa stanza, non puoi entrare"));
                } else {
                    return ResponseEntity.ok(new RispostaRichiestaAccessoStanza(true, "Sei già all'interno di questa stanza"));
                }

            } catch (RuntimeException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new RispostaRichiestaAccessoStanza(false, "Errore durante la richiesta: " + e.getMessage()));
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

    public Ruolo getRuolo(String nome){

        Ruolo ruolo = ruoloRepository.findByNome(nome);

        if(ruolo == null){
            ruolo = new Ruolo(nome);
            System.out.println(ruolo.toString());
            ruoloRepository.save(ruolo);
        }
        return ruolo;
    }

    @Override
    public ResponseEntity<RispostaRichiestaAccessoStanza> richiestaAccessoStanza(String codiceStanza, String id_utente) {
        try {
            Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);

            Utente u = utenteRepository.findFirstByMetaId(id_utente);
            StatoPartecipazione statoPartecipazione = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);

            if (statoPartecipazione == null) {
                statoPartecipazione = new StatoPartecipazione(stanza, u, getRuolo(Ruolo.PARTECIPANTE), true, false, u.getNome());
                return ResponseEntity.ok(new RispostaRichiestaAccessoStanza(true, "La stanza è privata, sei in attesa di entrare"));

            } else if (statoPartecipazione.isBannato()) {
                return ResponseEntity.ok(new RispostaRichiestaAccessoStanza(false, "Sei stato bannato da questa stanza, non puoi entrare"));

            } else if (statoPartecipazione.isInAttesa()) {
                return ResponseEntity.ok(new RispostaRichiestaAccessoStanza(true, "Sei già in attesa di entrare in questa stanza"));

            } else {
                return ResponseEntity.ok(new RispostaRichiestaAccessoStanza(true, "Sei già all'interno di questa stanza"));
            }


        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RispostaRichiestaAccessoStanza(false, "Errore durante la richiesta: " + e.getMessage()));
        }

    }
}
