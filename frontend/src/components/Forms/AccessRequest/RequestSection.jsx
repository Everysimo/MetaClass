import React, { useState, useEffect } from 'react';
import '../PopUpStyles.css';

const RequestSection = ({ id_stanza }) => {
    const [array, setArray] = useState([]);
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [message, setMessage] = useState("");
    console.log('idStanza', id_stanza);

    useEffect(() => {
        fetchGestioneAccessi();
        // eslint-disable-next-line
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
            const idStanza = id_stanza;
            const response = await fetch(
                `http://localhost:8080/visualizzaUtentiInAttesaInStanza/
                ${encodeURIComponent(idStanza)}`,
                requestOption
            );

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
    };

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
            console.log("stringa json:", requestOption);
            const response = await fetch(`http://localhost:8080/gestioneAccessi/${id_stanza}/${userid}`, requestOption);

            if (!response.ok) {
                throw new Error('Errore durante l\'accettazione della richiesta.');
            }

            const data = await response.json();
            console.log('data:', data);
            setMessage('Accettato');
        } catch (error) {
            console.error('Errore durante l\'accettazione della richiesta:', error.message);
            setMessage('Errore durante l\'accettazione della richiesta');
        }
    };

    const handleRejectButton = (userid) => {
        console.log("sono nel bottone rifiuta");
        console.log("idutente passato alla funzione:", userid);

        setSelectedUserId(userid);

        console.log("idutente nel select:", selectedUserId);
        handleReject();
    };

    const handleReject = async (userid) => {
        try {
            console.log("stringa json:", requestOption);
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

    const handleShowModal = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        if(message){
            setMessage('');
            window.location.replace(window.location.pathname);
        }
    };

    return (
        <>
            <button
                onClick={handleShowModal}
            >
                Gestione degli accessi
            </button>
            {showModal && (
                <div className={"modal"}>
                    <div className={"modal-content"}>
                        <span
                            className={"close"}
                            onClick={handleCloseModal}
                        >
                            &times;
                        </span>
                        {message ? (
                            <p>{message}</p>
                        ) : (
                            <div>
                                <h2>Richieste di accesso:</h2>
                                <div className={"table-container"}>
                                    {array && array.length > 0 ? (
                                        array.map((user) => (
                                            <div key={user.id} className="table-row">
                                                <span className={"table-cell"}>{`${user.nome} ${user.cognome}`}</span>
                                                <span className={"table-cell"}>
                                                    <button onClick={() => handleAcceptButton(user.id)}>Accetta</button>
                                                </span>
                                                <span className={"table-cell"}>
                                                    <button onClick={() => handleRejectButton(user.id)}>Rifiuta</button>
                                                </span>
                                            </div>
                                        ))
                                    ) : (
                                        <p
                                            style={{
                                                textAlign: "center",
                                                fontSize: "14px"
                                        }}>Non ci sono utenti in attesa</p>
                                    )}
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            )}
        </>
    );
};

export default RequestSection;