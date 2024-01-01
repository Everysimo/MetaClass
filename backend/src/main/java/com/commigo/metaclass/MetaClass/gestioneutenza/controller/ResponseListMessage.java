package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import java.util.List;

public class ResponseListMessage<T> {
    private List<T> successo;
    private String messaggio;

    // Costruttore
    public ResponseListMessage(List<T> successo, String messaggio) {
        this.successo = successo;
        this.messaggio = messaggio;
    }

    // Metodi getter e setter (opzionali, ma consigliati per la serializzazione JSON)
    public List<T> getSuccesso() {
        return successo;
    }

    public void setSuccesso(List<T> successo) {
        this.successo = successo;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}
