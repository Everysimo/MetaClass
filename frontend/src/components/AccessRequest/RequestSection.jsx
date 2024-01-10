import React, { useState, useEffect } from 'react';
import './RequestAccess.css';
import { useParams } from 'react-router-dom';

const RequestSection = () => {
    const [array, setArray] = useState([]);

    const { id: id_stanza } = useParams(); //si usa useParams per farsi passare il parametro
    console.log('idStanza', id_stanza);

    useEffect(() => {
        fetchGestioneAccessi();
    }, []);

    const requestOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + sessionStorage.getItem('token'),
        },
    };

    const fetchGestioneAccessi = async () => {
        try {
            const response = await fetch(`http://localhost:8080/visualizzaUtentiBannatiInStanza/${id_stanza}`, requestOption);

            if (!response.ok) {
                throw new Error('Errore richiesta not ok.');
            }

            const data = await response.json();
            console.log("eccoli i dati degli utenti in attesa:", data);

            setArray(data.value);
            console.log('lista utenti:', array);

        } catch (error) {
            console.error('Errore durante il recupero degli accessi:', error.message);
        }
    };
    /*
    const handleAccept = async (userId) => {
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

    const handleReject = async (userId) => {
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
    */
    return (
        <div className="access-management-container">
            <h2>Richieste di accesso:</h2>
            {array.map((user) => (
                <div key={user.id} className="user-card">
                    <span>{`${user.nome} ${user.cognome}`}</span>
                    <button onClick={() => {/*handleAccept(user.id)*/}}>Accetta</button>
                    <button onClick={() => {/*handleReject(user.id)*/}}>Rifiuta</button>
                </div>
            ))}
        </div>
    );
};

export default RequestSection;
