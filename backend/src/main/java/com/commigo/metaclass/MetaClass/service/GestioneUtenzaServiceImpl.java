package com.commigo.metaclass.MetaClass.service;

import com.commigo.metaclass.MetaClass.model.Utente;
import com.commigo.metaclass.MetaClass.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service("UserService")
public class GestioneUtenzaServiceImpl implements GestioneUtenzaService {

    @Autowired
    @Qualifier("UserRep")
    private UtenteRepository userRep;

    @Override
    public boolean loginMeta(Utente u) {
        try {
            //cerca l'utente per verificare se registrato o meno
            Optional<Utente> existingUser = userRep.findById(u.getId());

            if (existingUser.isEmpty()) {
                // Utente non presente nel database, lo salva
                userRep.save(u);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Stampa la traccia dell'eccezione per debugging
            return false;
        }
    }


}
