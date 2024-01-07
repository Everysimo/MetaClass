import React, { Component } from 'react';
import "./RequestAccess.css";

export default class RequestSection extends Component {
    constructor(props) {
        super(props);
        this.state = {
            array: []
        };
    }

    componentDidMount() {
        this.fetchGestioneAccessi();
    }

    requestOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + sessionStorage.getItem('token'),
        },
    };
    fetchGestioneAccessi = async () => {


        try {
            const response = await fetch('http://localhost:8080/gestioneAccessi', this.requestOption);

            if (!response.ok) {
                throw new Error('Errore richeista not ok.');
            }

            const data = await response.json();
            console.log("eccoli i dati degli utenti in attesa:", data);

        } catch (error) {
            console.error('Errore durante il recupero degli accessi:', error.message);
        }
    };

    handleAccept = async (userId) => {
        const requestOption = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userId),
        };

        try {
            const response = await fetch('http://localhost:8080', requestOption);
            const responseData = await response.json();

            console.log("Risposta dal server", responseData);
        } catch (error) {
            console.error('errore: ', error);
        }

        console.log(`Accettato l'accesso per l'utente con ID ${userId}`);
    };

    handleReject = async (userId) => {
        const requestOption = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userId),
        };

        try {
            const response = await fetch('http://localhost:8080', requestOption);
            const responseData = await response.json();

            console.log("Risposta dal server", responseData);
        } catch (error) {
            console.error('errore: ', error);
        }

        console.log(`Rifiutato l'accesso per l'utente con ID ${userId}`);
    };

    render() {
        return (
            <>
                <div className="access-management-container">
                    <h2>Richieste di accesso</h2>
                    {/*
                    {this.state.accessRequests.map((request) => (
                        <div key={request.userId} className="card-container">
                            <div className="user-info">
                                <h3>{request.userName}</h3>
                            </div>
                            <div className="button-container">
                                <button className={'button-field'} onClick={() => this.handleAccept(request.userId)}>Accetta</button>
                                <button className={'button-field'} onClick={() => this.handleReject(request.userId)}>Rifiuta</button>
                            </div>
                        </div>
                    ))} */}
                </div>

            </>
        );
    }
}