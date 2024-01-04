package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Ruolo;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;
import com.commigo.metaclass.MetaClass.gestioneutenza.exception.DataNotFoundException;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.response.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("GestioneUtenzaService")
@RequiredArgsConstructor
@Slf4j    //serve per stampare delle cose nei log
@Transactional    //ogni operazione è una transazione
public class GestioneUtenzaServiceImpl implements GestioneUtenzaService{

    private final UtenteRepository utenteRepository;
    private final StatoPartecipazioneRepository statoPartecipazioneRepository;
    private final StanzaRepository stanzaRepository;
    @Override
    public boolean loginMeta(Utente u) {
        try {
            //cerca l'utente per verificare se registrato o meno
            Utente existingUser = utenteRepository.findFirstByMetaId(u.getMetaId());
           if (existingUser==null) {
                // Utente non presente nel database, lo salva
                utenteRepository.save(u);
           }
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Stampa la traccia dell'eccezione per debugging
            return false;
        }
    }

    /**
     * @param SessionID
     * @param u
     * @return
     */
    @Override
    public Response<Boolean> modificaDatiUtente(String SessionID, Utente u) {

        try {
            Utente existingUser = utenteRepository.findFirstByMetaId(SessionID);
            if(existingUser == null) {
                return new Response<Boolean>(false, "l'utente non esiste");
            }else{
                if(utenteRepository.updateAttributes(SessionID, u)>0){
                    u = utenteRepository.findFirstByMetaId(SessionID);
                    return new Response<Boolean>(true, "modifica effettuata con successo");
                }else{
                    u = existingUser;
                    return new Response<Boolean>(true, "nessuna modifica effettuata");
                }
            }
        }catch (Exception e) {
            e.printStackTrace(); // Stampa la traccia dell'eccezione per debugging
            return new Response<Boolean>(false, "errore nella modifica dei dati");
        }
    }

    /**
     * @param MetaId
     * @return
     */
    @Override
    public List<Stanza> getStanzeByUserId(String MetaId) {
        try {
            Utente existingUser = utenteRepository.findFirstByMetaId(MetaId);
            if(existingUser == null) {
                throw new Exception("Utente non presente nel database");
            }else{
                List<StatoPartecipazione> stati =
                        statoPartecipazioneRepository.findAllByUtente(existingUser);
                if(stati==null){
                    throw new Exception("Errore nella ricerca delle stanze");
                }else{
                    // Estrai gli attributi 'stanza' dalla lista 'stati' e messi in una nuova lista
                    return stati.stream()
                            .map(StatoPartecipazione::getStanza)
                            .collect(Collectors.toList());
                }
            }
        }catch (Exception e) {
            e.printStackTrace(); // Stampa la traccia dell'eccezione per debugging
            return null;
        }
    }

/**
*
 * @param sessionID
 * @return
*/
    @Override
    public Utente getUtenteByUserId(String sessionID) throws DataNotFoundException {
            Utente existingUser = utenteRepository.findFirstByMetaId(sessionID);
            if(existingUser == null) {
                throw new DataNotFoundException("Utente non presente nel database");
            }else{
                return existingUser;
            }
    }
}

