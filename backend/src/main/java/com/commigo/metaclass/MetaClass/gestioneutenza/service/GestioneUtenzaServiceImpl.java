package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.exceptions.DataNotFoundException;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
           }else if(utenteRepository.updateAttributes(existingUser.getMetaId(),u)>0){
               return true;
           }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Response<Boolean> modificaDatiUtente(String MetaID, Utente u) {

            Utente existingUser = utenteRepository.findFirstByMetaId(MetaID);
            if(existingUser == null) {
                return new Response<>(false, "l'utente non esiste");
            }else{
                if(utenteRepository.updateAttributes(MetaID, u)>0){
                    u = utenteRepository.findFirstByMetaId(MetaID);
                    return new Response<>(true, "modifica effettuata con successo");
                }else{
                    u = existingUser;
                    return new Response<>(false, "nessuna modifica effettuata");
                }
            }
    }

    /**
     * @param MetaId
     * @return
     */
    @Override
    public List<Stanza> getStanzeByUserId(String MetaId) throws ServerRuntimeException{
            Utente existingUser = utenteRepository.findFirstByMetaId(MetaId);
            if(existingUser == null) {
                throw new ServerRuntimeException("Utente non presente nel database");
            }else{
                List<StatoPartecipazione> stati =
                        statoPartecipazioneRepository.findAllByUtente(existingUser);
                if(stati==null){
                    throw new ServerRuntimeException("Errore nella ricerca delle stanze");
                }else{
                    // Estrai gli attributi 'stanza' dalla lista 'stati' e messi in una nuova lista
                    return stati.stream()
                            .map(StatoPartecipazione::getStanza)
                            .collect(Collectors.toList());
                }
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

/**
*
 * @param token
 * @return
*/
    @Override
    public boolean logoutMeta(String token, ValidationToken validationToken) {
        System.out.println(token);
       Utente u = utenteRepository.findUtenteByTokenAuth(token);
       if(u==null)   return false;
       u.setTokenAuth(Utente.DEFAULT_TOKEN);
       if(utenteRepository.updateAttributes(u.getMetaId(), u)>0){
           return true;
       }
       return false;
    }
}

