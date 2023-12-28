package com.commigo.metaclass.MetaClass.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NotSet' NOT NULL")
    private String Nome;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NotSet' NOT NULL")
    private String Cognome;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT 'M' NOT NULL")
    private String Sesso;

    @Column(columnDefinition = "INT DEFAULT 0 NOT NULL")
    private int Età;

    @Column(columnDefinition = "BOOLEAN DEFAULT false NOT NULL")
    private boolean isAdmin;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'default@example.com' NOT NULL")
    private String Email;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NotSet'")
    private String Telefono;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NotSet' NOT NULL")
    private String idMeta;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NotSet' NOT NULL")
    private String TokenAuth;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL")
    private LocalDateTime DataCreazione;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL")
    private LocalDateTime DataAggiornamento;

    // Costruttore vuoto necessario per JPA
    public Utente() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public int getEtà() {
        return Età;
    }

    public void setEtà(int età) {
        Età = età;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getSesso() {
        return Sesso;
    }

    public void setSesso(String sesso) {
        Sesso = sesso;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(String idMeta) {
        this.idMeta = idMeta;
    }

    public String getTokenAuth() {
        return TokenAuth;
    }

    public void setTokenAuth(String tokenAuth) {
        TokenAuth = tokenAuth;
    }

    //costruttore da fare
}
