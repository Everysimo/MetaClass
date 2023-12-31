package com.commigo.metaclass.MetaClass.gestioneutenza.service;

import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.gestioneutenza.controller.ResponseBoolMessage;
import com.commigo.metaclass.MetaClass.gestioneutenza.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("GestioneUtenzaService")
@RequiredArgsConstructor
@Slf4j    //serve per stampare delle cose nei log
@Transactional    //ogni operazione è una transazione
public class GestioneUtenzaServiceImpl implements GestioneUtenzaService{

    private final UtenteRepository utenteRepository;
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
     * @param Id
     * @param dataMap
     * @param u
     * @return
     */
    @Override
    public ResponseBoolMessage modificaDatiUtente(Long Id, Map<String, Object> dataMap, Utente u) {

        try {
            Utente existingUser = utenteRepository.findUtenteById(Id);
            if(existingUser == null) {
                return new ResponseBoolMessage(false, "l'utente non esiste");
            }else{
                if(utenteRepository.updateAttributes(Id, dataMap)>0){
                    u = utenteRepository.findUtenteById(Id);
                    return new ResponseBoolMessage(true, "modifica effettuata con successo");
                }else{
                    u = existingUser;
                    return new ResponseBoolMessage(true, "nessuna modifica effettuata");
                }
            }
        }catch (Exception e) {
            e.printStackTrace(); // Stampa la traccia dell'eccezione per debugging
            return new ResponseBoolMessage(false, "errore nella modifica dei dati");
        }
    }
}
