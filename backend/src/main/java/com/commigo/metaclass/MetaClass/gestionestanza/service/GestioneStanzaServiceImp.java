package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.utility.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service("GestioneStanzaService")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GestioneStanzaServiceImp implements GestioneStanzaService
{
    private final StanzaRepository stanzaRepository;
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
}
