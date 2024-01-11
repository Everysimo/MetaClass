import React, { useState, useEffect } from 'react';
import './RequestAccess.css';
import { useParams } from 'react-router-dom';

const RequestSection = () => {
    const [array, setArray] = useState([]);
    const [selectedUserId, setSelectedUserId] = useState(null);

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
            const response = await fetch(`http://localhost:8080/visualizzaUtentiInAttesaInStanza/${encodeURIComponent(id_stanza)}`, requestOption);

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

    const handleAcceptButton = (userid) => {
        console.log("sono nel bottone accetta");
        console.log("idutente passato alla funzione:", userid);


        handleAccept(userid);
    }
    const handleAccept = async (userid) => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify({ scelta: true })
        };

        try {
            console.log("stringa json:",requestOption )
            const response = await fetch(`http://localhost:8080/gestioneAccessi/${id_stanza}/${userid}`, requestOption);

            if (!response.ok) {
                throw new Error('Errore durante l\'accettazione della richiesta.');
            }

            const data = await response.json();
            console.log('data:', data);

        } catch (error) {
            console.error('Errore durante l\'accettazione della richiesta:', error.message);
        }
    };

    const handleRejectButton = (userid) => {
        console.log("sono nel bottone rifiuta");
        console.log("idutente passato alla funzione:", userid);

        setSelectedUserId(userid);

        console.log("idutente nel select:", selectedUserId);
        handleReject();
    }
    const handleReject = async (userid) => {
        try {
            console.log("stringa json:",requestOption )
            const response = await fetch(`http://localhost:8080/gestioneAccessi/${id_stanza}/${userid}`, requestOption);


            if (!response.ok) {
                throw new Error('Errore durante il rifiuto della richiesta.');
            }

            const data = await response.json();
            console.log('data:', data);
        } catch (error) {
            console.error('Errore durante il rifiuto della richiesta:', error.message);
        }
    };

    return (
        <div className="access-management-container">
            <h2>Richieste di accesso:</h2>
            {array.map((user) => (
                <div key={user.id} className="user-card">
                    <span>{`${user.nome} ${user.cognome}`}</span>
                    <button onClick={() => handleAcceptButton(user.id)}>Accetta</button>
                    <button onClick={() => handleRejectButton(user.id)}>Rifiuta</button>
                </div>
            ))}
        </div>
    );
};

export default RequestSection;
