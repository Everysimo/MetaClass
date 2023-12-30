package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

public class RispostaLoginAndLogout {

    private boolean successo;
    private String messaggio;

    // Costruttore
    public RispostaLoginAndLogout(boolean successo, String messaggio) {
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

