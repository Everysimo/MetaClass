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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service("GestioneUtenzaService")
@RequiredArgsConstructor
@Slf4j    //serve per stampare delle cose nei log
@Transactional
//ogni operazione è una transazione
public class GestioneUtenzaServiceImpl implements GestioneUtenzaService{

    private final UtenteRepository utenteRepository;
    private final StatoPartecipazioneRepository statoPartecipazioneRepository;
    private final StanzaRepository stanzaRepository;

    private final Set<String> adminMetaIds = loadAdminMetaIdsFromFile();

    private Set<String> loadAdminMetaIdsFromFile() {
        Set<String> adminIds = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("admins.txt").getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                adminIds.add(line.trim());
            }
        } catch (IOException e) {
            // Gestione eccezioni legate alla lettura del file (ad esempio FileNotFoundException)
            e.printStackTrace();
        }
        return adminIds;
    }
    @Override
    public boolean loginMeta(Utente u) {
        try {
            //cerca l'utente per verificare se registrato o meno
            Utente existingUser = utenteRepository.findFirstByMetaId(u.getMetaId());
           if (existingUser==null) {
                // Utente non presente nel database, lo salva
                utenteRepository.save(u);
           }else{
               if(adminMetaIds.contains(existingUser.getMetaId())){
                   existingUser.setAdmin(true);
               }
               utenteRepository.updateAttributes(existingUser.getMetaId(),u);
           }
           return true;
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

