import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const AllUserList = () => {
    const [userList, setUserList] = useState([]);

    const { id: id_stanza } = useParams();
    console.log('idStanza', id_stanza);

    useEffect(() => {
        fetchUserList();
    }, []);

    const requestOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchUserList = async () => {
        try {
            const response = await fetch(`http://localhost:8080/visualizzaUtentiInStanza/${id_stanza}`, requestOption);

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
    const handleBan = (userId) => {
        // Implementa la logica per revocare il ban per l'utente con l'ID fornito
        console.log(`Revoca ban per l'utente con ID ${userId}`);
    };
    const handlekick = (userId) => {
        // Implementa la logica per revocare il ban per l'utente con l'ID fornito
        console.log(`Revoca ban per l'utente con ID ${userId}`);
    };



    return (
        <div>
            <h2>Lista Utenti:</h2>
            {userList && userList.map((user) => (
                <div key={user.id} className="user-card">
                    <span>{`${user.nome} ${user.cognome}`}</span>
                    <button onClick={() => handleBan(user.id)}>Banna Utente</button>
                    <button onClick={() => handlekick(user.id)}>Kick Utente</button>
                </div>
            ))}
        </div>
    );
};

export default AllUserList;
