import { useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";
import './UserList.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAlignCenter} from "@fortawesome/free-solid-svg-icons";

const UserListInRoom = () => {
    const [userList, setUserList] = useState([]);
    const [isPopupOpen, setPopupOpen] = useState(false);
    const [newName, setNewName] = useState("");
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [showButtonsMap, setShowButtonsMap] = useState({});

    const { id: id_stanza } = useParams();

    useEffect(() => { fetchUserList(); }, []);

    // Function to toggle button visibility for a specific user card
    const toggleButtons = (userId) => {
        setShowButtonsMap(prevState => ({
            ...prevState,
            [userId]: !prevState[userId] || false,
        }));
    };

    const requestOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchUserList = async () => {
        try {
            const response = await fetch(
                `http://localhost:8080/visualizzaUtentiInStanza/${id_stanza}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta');
            }
            const data = await response.json();
            setUserList(data.value);
        } catch (error) {
            console.error('Errore durante il recupero della lista di utenti:', error);
        }
    }

    const handleChangeNameButton = (idutente) => {
        console.log("idutente", idutente);
        setSelectedUserId(idutente);
        setPopupOpen(true);
    }

    //da aggiustare il fatto del popup
    const handleChangeName = async () => {
        console.log("newname", newName)
        const nome = newName;

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify({ nome })
        };
        try {
            console.log("stringa json:",requestOption )
            const response = await fetch(
                `http://localhost:8080/modificaNomePartecipante/${id_stanza}/${selectedUserId}`,
                requestOption
            );
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
            const response = await fetch(
                `http://localhost:8080/kickarePartecipante/${id_stanza}/${selectedUserId}`,
                requestOption
            );
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
        handleSilenziaUser(idutente);
    }
    const handleSilenziaUser = async (idutente) => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };
        try {
            const response = await fetch(
                `http://localhost:8080/silenziarePartecipante/${id_stanza}/${idutente}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta di silenziare partecipante');
            }
            const data = await response.json();
            console.log('data:', data);
        } catch (error) {
            console.error('Errore durante la richiesta di silenziare il partecipante:', error);
        }
    }

    const handlePromotionButton = (idutente) => {
        console.log(idutente);
        setSelectedUserId(idutente);
        handlePromotion();
    };

    const handlePromotion = async () => {
        console.log('Before fetch call:', id_stanza, selectedUserId);

        if (!id_stanza || !selectedUserId) {
            console.error('Invalid id_stanza or selectedUserId');
            return;
        }

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };

        try {
            console.log('About to make fetch call');
            const response = await fetch(
                `http://localhost:8080/promuoviOrganizzatore/${id_stanza}/${selectedUserId}`,
                requestOption
            );
            console.log('Fetch call completed');
            console.log(response);
            if (!response.ok) {
                throw new Error('Error in promoting the user');
            }

            const data = await response.json();
            console.log('data:', data);
        } catch (error) {
            console.error('Error during user promotion:', error);
        }
    };

    const handleBanUserButton = (idutente) => {
        console.log("idutente", idutente);
        setSelectedUserId(idutente);
        handleBanUser();
    }
    const handleBanUser = async () => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };
        try {
            const response = await fetch(
                `http://localhost:8080/banUtente/${id_stanza}/${selectedUserId}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta di Bannare il partecipante');
            }
            const data = await response.json();
            console.log('data:', data);
        } catch (error) {
            console.error('Errore durante il ban dell\'utente:', error);
        }
    }

    const handleDeclassifyButton = (idutente) => {
        console.log(idutente);
        setSelectedUserId(idutente);
        handleDeclassify();
    };

    const handleDeclassify = async () => {
        console.log('Before fetch call:', id_stanza, selectedUserId);

        if (!id_stanza || !selectedUserId) {
            console.error('Invalid id_stanza or selectedUserId');
            return;
        }

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };

        try {
            console.log('fetch call effettuata ');
            const response = await fetch(
                `http://localhost:8080/declassaOrganizzatore/{IdStanza}/{IdUtente}`,
                requestOption
            );
            console.log('Fetch call completed');
            console.log(response);
            if (!response.ok) {
                throw new Error('Errore nella richiesta di declassare');
            }

            const data = await response.json();
            console.log('data:', data);
        } catch (error) {
            console.error('Errore nella richiesta di declassare:', error);
        }
    };

    return (
        <div>
            <h2>Utenti In Stanza:</h2>
            {userList && userList.map((user) => (
                <div key={user.id} className="user-card">
                    <span>Nome: {`${user.nome} ${user.cognome}`}</span><br />
                    <span>Email: {`${user.email}`}</span>
                    <button onClick={() => toggleButtons(user.id)}>
                        Options <FontAwesomeIcon icon={faAlignCenter} style={{color: "#ffffff",}} />
                    </button>
                    <div className={`options-container${showButtonsMap[user.id] ? ' open' : ''}`}>
                        <button onClick={() => handleChangeNameButton(user.id)}>Cambia Nome</button>
                        <button onClick={() => handleKickUserButton(user.id)}>Kicka Partecipante</button>
                        <button onClick={() => handleSilenziaUserButton(user.id)}>Silenzia Partecipante</button>
                        <button onClick={() => handlePromotionButton(user.id)}>Promuovi</button>
                        <button onClick={() => handleBanUserButton(user.id)}>Banna Partecipante</button>
                        <button onClick={() => handleDeclassifyButton(user.id)}>Declassa</button> {/*Non Funziona*/}
                    </div>
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