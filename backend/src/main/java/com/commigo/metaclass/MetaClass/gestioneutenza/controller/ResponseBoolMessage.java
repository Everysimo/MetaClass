package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

public class ResponseBoolMessage {

    private boolean successo;
    private String messaggio;

    // Costruttore
    public ResponseBoolMessage(boolean successo, String messaggio) {
        this.successo = successo;
        this.messaggio = messaggio;
    }

    // Metodi getter e setter (opzionali, ma consigliati per la serializzazione JSON)
    public boolean isSuccesso() {
        return successo;
    }

    public void setSuccesso(boolean successo) {
        this.successo = successo;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}

