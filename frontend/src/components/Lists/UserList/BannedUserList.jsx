import React, { useEffect, useState } from 'react';

const BannedUserList = ({ id_stanza }) => {
    const [userList, setUserList] = useState([]);
    const [message, setMessage] = useState('');
    const [showModal, setShowModal] = useState(false);
    const isAdmin = sessionStorage.getItem('isAdmin');

    useEffect(() => {
        fetchBannedUserList();
    }, []);

    const requestOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchBannedUserList = async () => {
        try {
            //const idStanza = id_stanza_int;
            const response = await fetch(
                `http://localhost:8080/admin/visualizzaUtentiBannatiInStanza/${id_stanza}`,
                requestOption
            );

            if (!response.ok) {
                throw new Error('Errore nella richiesta');
            }

            const data = await response.json();
            console.log('data:', data);

            setUserList(data.value);
            console.log('lista utenti:', userList);
        } catch (error) {
            console.error('Errore durante il recupero della lista di utenti:', error);
        }
    }

    const handleRevocaBanButton = (idutente) => {
        console.log("idUtente qui:", idutente)
        handleRevocaBan(idutente);
    }

    const handleRevocaBan = async (idutente) => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };
        try {
            const response = await fetch(`http://localhost:8080/admin/annullaBan/${id_stanza}/${idutente}`, requestOption);
            if (!response.ok) {
                throw new Error('Errore nella richiesta di revocare il ban');
            }
            const data = await response.json();
            console.log('data:', data);
            setMessage(data.message);
        } catch (error) {
            console.error('Errore durante la revoca del ban:', error);
        }
        console.log(`Revoca ban per l'utente`);
    };

    const show=()=>{
        setShowModal(true);
    }
    const close=()=>{
        setShowModal(false);
        setMessage('');
    }
    const checkAdm = () => {
        console.log('isAdmin: ', isAdmin);
        return isAdmin === "true";
    }

    if (checkAdm()) {
        return (
            <>
                <button onClick={show}>
                    Utenti bannati
                </button>
                {showModal &&
                    <div className={'modal'}>
                        <div className={'modal-content'}>
                            <span className={'close'} onClick={close}>
                              &times;
                            </span>
                            {message ? (
                                <p>{message}</p>
                                ) : (
                                    <>
                            <h2>Utenti Bannati:</h2>
                            {userList && userList.map((user) => (
                                <div key={user.id} className="user-card">
                                    <span>{`${user.nome} ${user.cognome}`}</span>
                                    <button
                                        onClick={() => handleRevocaBanButton(user.id)}
                                    >
                                        Revoca Ban Utente
                                    </button>
                                </div>
                            ))}
                                    </>)}
                        </div>
                    </div>
                }
            </>
        );
    } else {
        return null;
    }
};

export default BannedUserList;