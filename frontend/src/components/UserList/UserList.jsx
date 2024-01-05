// UserList.jsx
import React, {useEffect} from 'react';
import UserCard from './UserCard';
import axios from "axios";

const UserList = () => {

    const userList = [
        { id: 1, name: 'John Doe', lunghezzapene: '15cm' },
        { id: 2, name: 'Jane Smith', lunghezzapene: '15cm'  },
        { id: 3, name: 'Bob Johnson', lunghezzapene: '15cm'  },
        // Aggiungi altri utenti se necessario
    ];
/*

    useEffect(() => {
        // Funzione per prelevare la lista di utenti dal backend
        const fetchUserList = async () => {
            try {
                const response = await axios.get('http://localhost:8080/...'); // Sostituisci 'URL_DEL_BACKEND' con il vero URL del tuo backend
                setUserList(response.data);
            } catch (error) {
                console.error('Errore durante il recupero della lista di utenti:', error);
            }
        };

        // Chiamata alla funzione per ottenere i dati dal backend
        fetchUserList();
    }, []); // L'array vuoto come secondo argomento assicura che useEffect venga eseguito solo una volta al montaggio del componente
*/
    return (
        <div>
            <h2>User List</h2>
            {userList.map((user) => (
                <UserCard key={user.id} user={user} />
            ))}
        </div>
    );
};

export default UserList;
