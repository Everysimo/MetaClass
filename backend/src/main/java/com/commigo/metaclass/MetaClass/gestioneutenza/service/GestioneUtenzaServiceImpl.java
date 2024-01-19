package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.StatoPartecipazione;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.exceptions.DataNotFoundException;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j    //serve per stampare delle cose nei log
@Transactional
//ogni operazione è una transazione
public class GestioneUtenzaServiceImpl implements GestioneUtenzaService{

    private final UtenteRepository utenteRepository;
    private final StatoPartecipazioneRepository statoPartecipazioneRepository;
    private final StanzaRepository stanzaRepository;
    @Autowired
    private ValidationToken validationToken;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private final Set<String> adminMetaIds = loadAdminMetaIdsFromFile();

    /**
     *
     * @return
     */
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

    /**
     *
     * @param u
     * @return
     * @throws ServerRuntimeException
     */
    @Override
    public boolean loginMeta(Utente u) throws ServerRuntimeException {
        try {

            if(adminMetaIds.contains(u.getMetaId())){
                u.setAdmin(true);
            }

            Utente ut;
            if((ut=utenteRepository.findFirstByMetaId(u.getMetaId())) != null){
                ut.setTokenAuth(u.getTokenAuth());
                utenteRepository.save(ut);
            }else
                utenteRepository.save(u);

            return true;
        }catch (DataIntegrityViolationException e){
            throw new ServerRuntimeException("errore nella registrazione dell'utente");
        }
    }

    /**
     *
     * @param MetaID
     * @param params
     * @return
     * @throws RuntimeException403
     */
    @Override
    public boolean modificaDatiUtente(String MetaID, Map<String, Object> params) throws RuntimeException403{

        String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

        Utente u = utenteRepository.findFirstByMetaId(metaID);

        if(u == null) {
            throw new RuntimeException403("Utente non registrato nei sistemi");
        }

        return utenteRepository.updateAttributes(u.getMetaId(), params)>0;
    }

    /**
     *
     * @param MetaId
     * @return
     * @throws ServerRuntimeException
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
     * @throws DataNotFoundException
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
     * @param metaID
     * @param validationToken
     * @return
     * @throws ServerRuntimeException
     */
    @Override
    public boolean logoutMeta(String metaID, ValidationToken validationToken) throws ServerRuntimeException {

       Utente u = utenteRepository.findFirstByMetaId(metaID);
       if(u==null)
           return false;

       u.setTokenAuth(Utente.DEFAULT_TOKEN);
       try {
           utenteRepository.save(u);
            return true;
       }catch (DataIntegrityViolationException e){
           throw new ServerRuntimeException("errore nel salvataggio del utente");
       }

    }
}

