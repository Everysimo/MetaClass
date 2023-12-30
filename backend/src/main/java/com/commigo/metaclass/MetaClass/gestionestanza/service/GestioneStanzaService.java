package com.commigo.metaclass.MetaClass.gestionestanza.service;

import com.commigo.metaclass.MetaClass.entity.Stanza;

public interface GestioneStanzaService
{

    /**
     *
     * Questo metodo crea una nuova stanza.
     * La istanza della stanza creata viene ritornata.
     *
     * @param nome
     * @param Codice_Stanza
     * @param Descrizione
     * @param Tipo_Accesso
     * @param MAX_Posti
     * @return
     */
    Stanza creaStanza(String nome, String Codice_Stanza, String Descrizione, boolean Tipo_Accesso, int MAX_Posti);

}
