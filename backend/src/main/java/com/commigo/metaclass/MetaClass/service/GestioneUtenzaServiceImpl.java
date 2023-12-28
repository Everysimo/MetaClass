package com.commigo.metaclass.MetaClass.service;

import com.commigo.metaclass.MetaClass.model.Utente;
import org.springframework.stereotype.Service;

@Service("UserService")
public class GestioneUtenzaServiceImpl implements GestioneUtenzaService{
    @Override
    public Utente loginMeta() {
        return new Utente();
    }

}
