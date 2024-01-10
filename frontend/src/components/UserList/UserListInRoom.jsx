import { useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";

const UserListInRoom = () => {
    const [userList, setUserList] = useState([]);
    const [isPopupOpen, setPopupOpen] = useState(false);
    const [newName, setNewName] = useState("");
    const [selectedUserId, setSelectedUserId] = useState(null);

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

    const handleChangeNameButton = (idutente) => {
        console.log("idutente", idutente);
        setSelectedUserId(idutente);
        setPopupOpen(true);
    }

    const handleChangeName = async () => {
        setPopupOpen(false); // Chiudi il popup quando si conferma il cambio nome

        console.log("newname", newName)

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify({ newName })
        };

        try {
            console.log("stringa json:",requestOption )

            const response = await fetch(`http://localhost:8080/modificaNomePartecipante/${id_stanza}/${selectedUserId}`, requestOption);

            if (!response.ok) {
                throw new Error('Errore nella richiesta di cambio nome');
            }

            const data = await response.json();
            console.log('data:', data);



        } catch (error) {
            console.error('Errore durante la modifica del nome:', error);
        }
    }

    const handleKickUserButton = (idutente) => {
        console.log("idutente", idutente);
        setSelectedUserId(idutente);
        handleKickUser();
    }
    const handleKickUser = async () => {

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };

        try {
            const response = await fetch(`http://localhost:8080/kickarePartecipante/${id_stanza}/${selectedUserId}`, requestOption);

            if (!response.ok) {
                throw new Error('Errore nella richiesta di kickare partecipante');
            }

            const data = await response.json();
            console.log('data:', data);


        } catch (error) {
            console.error('Errore durante il kick dell\'utente:', error);
        }
    }

    const handleSilenziaUserButton = (idutente) => {
        console.log("idutente", idutente);
        setSelectedUserId(idutente);
        handleSilenziaUser();
    }
    const handleSilenziaUser = async () => {

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };

        try {
            const response = await fetch(`http://localhost:8080/.../${id_stanza}/${selectedUserId}`, requestOption);

            if (!response.ok) {
                throw new Error('Errore nella richiesta di silenziare partecipante');
            }

            const data = await response.json();
            console.log('data:', data);



        } catch (error) {
            console.error('Errore durante la richiesta di silenziare il partecipante:', error);
        }
    }

    return (
        <div>
            <h2>Utenti In Stanza:</h2>
            {userList && userList.map((user) => (
                <div key={user.id} className="user-card">
                    <span>Nome: {`${user.nome} ${user.cognome}`}</span><br/>
                    <span>Email: {`${user.email}`}</span>
                    {/* Aggiungi il pulsante e passa l'idutente a handleChangeNameButton */}
                    <button onClick={() => handleChangeNameButton(user.id)}>Cambia Nome</button>
                    <button onClick={() => handleKickUserButton(user.id)}>Kicka Partecipante</button>
                    <button onClick={() => handleSilenziaUserButton(user.id)}>Silenzia Partecipante</button>

                </div>
            ))}

            {/* Popup Modale */}
            {isPopupOpen && (
                <div className="popup-container">
                    <div className="popup">
                        <label>
                            Nuovo Nome:
                            <input
                                type="text"
                                value={newName}
                                onChange={(e) => setNewName(e.target.value)}
                            />
                        </label>
                        <button onClick={handleChangeName}>Conferma</button>
                        <button onClick={() => setPopupOpen(false)}>Annulla</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserListInRoom;
