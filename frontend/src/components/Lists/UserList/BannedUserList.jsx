import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const BannedUserList = ({id_stanza}) => {
    const [userList, setUserList] = useState([]);
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const id_stanza_int = parseInt(id_stanza, 10);
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
            const response = await fetch(`http://localhost:8080/admin/visualizzaUtentiBannatiInStanza/${id_stanza_int}`, requestOption);

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
        } catch (error) {
            console.error('Errore durante la revoca del ban:', error);
        }
        console.log(`Revoca ban per l'utente }`);
    };

    return (
        <>
            <div className={'modal'}>
                <div className={'modal-content'}>
                    <span
                        className={'close'}
                    >
                        &times;
                    </span>
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
                </div>
            </div>
        </>
    );
};

export default BannedUserList;
