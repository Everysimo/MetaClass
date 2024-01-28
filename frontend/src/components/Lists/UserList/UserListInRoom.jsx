import { useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";
import './UserList.css';
import '../../Forms/PopUpStyles.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faAlignCenter,
    faArrowsRotate, faArrowTrendDown, faArrowTrendUp, faBan,
    faCrown,
    faMicrophone,
    faMicrophoneSlash, faSnowplow
} from "@fortawesome/free-solid-svg-icons";
import { checkRole } from "../../../functions/checkRole";

const UserListInRoom = () => {
    const [userList, setUserList] = useState([]);
    const [showButtonsMap, setShowButtonsMap] = useState({});
    const [showChangeNameModal, setChangeNameModal] = useState(false);
    const [errore, setErrore] = useState(null);
    const [showSuccessPopup, setShowSuccessPopup] = useState(false);
    const [message, setMessage] = useState('');
    const [newName, setNewName] = useState("");
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [statoPartecipazione, setStatoP] = useState(null);

    const { id: id_stanza } = useParams();
    const id_utente = sessionStorage.getItem('UserMetaID');
    const idStanza = id_stanza;

    useEffect(() => {
        fetchUserList();
        fetchstatoPartecipazione();
    }, []);

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
                `http://localhost:8080/visualizzaUtentiInStanza/${idStanza}`,
                requestOption
            );

            if (!response.ok) {
                throw new Error('Errore nella richiesta');
            }

            const data = await response.json();
            setUserList(data.value);

            const rolesPromises = data.value.map(user => checkOrg(id_stanza, user.id));
            const roles = await Promise.all(rolesPromises);

            setUserList(prevUserList => prevUserList.map((user, index) => ({
                ...user,
                role: roles[index],
            })));

        } catch (error) {
            console.error('Errore durante il recupero della lista di utenti:', error);
        }
    }

    const fetchstatoPartecipazione = async() => {
        try {
            console.log("sono qui dentro")

            const response = await fetch(`http://localhost:8080/getStatopartecipazione/${idStanza}`, requestOption);

            if (!response.ok) {
                throw new Error('Errore nella richiesta');
            }

            const data = await response.json();

            console.log("dati:", data);

            setStatoP(data);


            if (Array.isArray(data.value) && data.value.length > 0) {
                // Stampa il nome in stanza di ciascun elemento nell'array
                data.value.forEach((element, index) => {
                    console.log(`Nome in stanza ${index + 1}: ${element.nomeInStanza}`);
                });
            } else {
                console.log("Nessun dato disponibile.");
            }

            console.log("Stato partecipazioni:", statoPartecipazione);

        } catch (error) {
            console.error('Errore durante il recupero della lista di utenti:', error);
        }
    }

    const handleChangeNameButton = (idUtente) => {
        setSelectedUserId(idUtente);
        setChangeNameModal(true);
    }

    const handleChangeName = async () => {
        if (newName === '') {
            setErrore('Il nome non può essere vuoto.');
            return;
        }

        const capitalizedNewName = newName.charAt(0).toUpperCase() + newName.slice(1);
        setErrore(null);

        const nome = capitalizedNewName;

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify({ nome })
        };
        try {
            const response = await fetch(
                `http://localhost:8080/modificaNomePartecipante/${id_stanza}/${selectedUserId}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta di cambio nome');
            }

            if (response.ok) {
                const data = await response.json();
                handleCloseChangeNamePopUp();
                setMessage(data.message)
                setShowSuccessPopup(true);

            } else {
                console.error('Errore durante l\'invio della valutazione al backend');
            }

        } catch (error) {
            console.error('Errore durante la modifica del nome:', error);
        }
    }

    const handleKickUserButton = (idUtente) => {
        handleKickUser(idUtente);
    }

    const handleKickUser = async (idUtente) => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };
        try {
            const response = await fetch(
                `http://localhost:8080/kickarePartecipante/${id_stanza}/${idUtente}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta di kickare partecipante');
            }

            if(response.ok){
                const data = await response.json();
                setMessage(data.message)
                setShowSuccessPopup(true);
            }

        } catch (error) {
            console.error('Errore durante il kick dell\'utente:', error);
        }
    }

    const handleSilenziaUserButton = (idUtente) => {
        handleSilenziaUser(idUtente);
    }

    const handleSilenziaUser = async (idUtente) => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };
        try {
            const response = await fetch(
                `http://localhost:8080/silenziarePartecipante/${idStanza}/${idUtente}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta di silenziare partecipante');
            }

            if(response.ok){
                const data = await response.json();
                setMessage(data.message)
                setShowSuccessPopup(true);
            }

        } catch (error) {
            console.error('Errore durante la richiesta di silenziare il partecipante:', error);
        }
    }

    const handleUnMuteUserButton = (idUtente) => {
        handleUnMuteUser(idUtente);
    }

    const handleUnMuteUser= async(idUtente) => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };
        try {
            const response = await fetch(
                `http://localhost:8080/unmutePartecipante/${idStanza}/${idUtente}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta di smutare partecipante');
            }

            if(response.ok){
                const data = await response.json();
                setMessage(data.message)
                setShowSuccessPopup(true);
            }

        } catch (error) {
            console.error('Errore durante la richiesta di smutare il partecipante:', error);
        }
    }

    const handlePromotionButton = (idUtente) => {
        setSelectedUserId(idUtente);
        handlePromotion(idUtente);
    };

    const handlePromotion = async (idUtente) => {
        if (!id_stanza || !idUtente) {
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
            const response = await fetch(
                `http://localhost:8080/promuoviOrganizzatore/${idStanza}/${idUtente}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Error in promoting the user');
            }

            if(response.ok){
                const data = await response.json();
                setMessage(data.message)
                setShowSuccessPopup(true);
            }
        } catch (error) {
            console.error('Error during user promotion:', error);
        }
    };

    const handleBanUserButton = (idUtente) => {
        setSelectedUserId(idUtente);
        handleBanUser(idUtente);
    }

    const handleBanUser = async (idUtente) => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };
        try {
            const response = await fetch(
                `http://localhost:8080/banUtente/${idStanza}/${idUtente}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta di Bannare il partecipante');
            }

            if(response.ok){
                const data = await response.json();
                setMessage(data.message)
                setShowSuccessPopup(true);
            }
        } catch (error) {
            console.error('Errore durante il ban dell\'utente:', error);
        }
    }

    const handleDeclassifyButton = (idUtente) => {
        setSelectedUserId(idUtente);
        handleDeclassify(idUtente);
    };

    const handleDeclassify = async (idUtente) => {
        if (!id_stanza || !idUtente) {
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
            const response = await fetch(
                `http://localhost:8080/declassaOrganizzatore/${idStanza}/${idUtente}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta di declassare');
            }

            if(response.ok){
                const data = await response.json();
                setMessage(data.message)
                setShowSuccessPopup(true);
            }

        } catch (error) {
            console.error('Errore nella richiesta di declassare:', error);
        }
    };

    const handleCloseChangeNamePopUp = () => {
        setChangeNameModal(false);
    }

    const handleCloseSuccesPopUp = ()=> {
        setTimeout(() => {
            setShowSuccessPopup(false);
            window.location.replace(window.location.pathname);
        }, 1000);
    };

    const checkOrg = async (id_stanza, userId) => {
        try {
            const fetchedRole = await checkRole(id_stanza, userId);
            if(fetchedRole === "Partecipante")
                return 0;
            else if(fetchedRole === "Organizzatore")
                return 1;
            else
                return 2;
        } catch (error) {
            console.error(error);
            return false;
        }
    }

    const utentiSilenziati = statoPartecipazione?.value?.filter(
        item => item.silenziato
    )?.map(item => item.utente.id) || [];

    return (

        <div>
            <h2>Utenti In Stanza:</h2>
            {userList && userList.map((user) => (

                <div key={user.id} className="user-card">
                    {
                        statoPartecipazione &&
                        statoPartecipazione.value &&
                        Array.isArray(statoPartecipazione.value) &&
                        statoPartecipazione.value.find((partecipazione) => partecipazione.utente.id === user.id &&
                            partecipazione.ruolo.nome === 'Organizzatore') && (
                            <div>
                                <FontAwesomeIcon icon={faCrown} />
                            </div>
                        )
                    }
                    {
                        statoPartecipazione &&
                        statoPartecipazione.value &&
                        Array.isArray(statoPartecipazione.value) &&
                        statoPartecipazione.value.find((partecipazione) => partecipazione.utente.id === user.id &&
                            partecipazione.ruolo.nome === 'Organizzatore_Master') && (
                            <div>
                                <FontAwesomeIcon
                                    icon={faCrown}
                                    style={{
                                        color: "yellow",
                                        backgroundColor: "#002f53",
                                        borderRadius: "2px"
                                }}/>
                            </div>
                        )
                    }
                    <span>Nome: {`${user.nome} ${user.cognome}`}</span>
                    <span>Nome In Stanza: {
                        statoPartecipazione &&
                        statoPartecipazione.value &&
                        Array.isArray(statoPartecipazione.value) ?
                        statoPartecipazione.value.find(
                            (partecipazione) => partecipazione.utente.id === user.id)?.nomeInStanza ||
                            "N/A":"N/A"
                    }
                    </span>
                    <span>Email: {`${user.email}`}</span>
                        <>
                            {
                                statoPartecipazione &&
                                statoPartecipazione.value &&
                                Array.isArray(statoPartecipazione.value) &&
                                statoPartecipazione.value.find(
                                    (partecipazione) => partecipazione.utente.id === user.id &&
                                    partecipazione.ruolo.nome !== 'Organizzatore_Master') && (
                                        <button onClick={() => toggleButtons(user.id)}>
                                            Opzioni <FontAwesomeIcon
                                            icon={faAlignCenter}
                                            style={{color: "#ffffff",}}/>
                                        </button>
                                )
                            }
                            <div className={`options-container${showButtonsMap[user.id] ? ' open' : ''}`}>
                                <button
                                    onClick={() => handleChangeNameButton(user.id)}
                                >
                                    Cambia Nome <FontAwesomeIcon icon={faArrowsRotate} style={{ color: "#ffffff", }} />
                                </button>
                                {(user.role === 1 || user.role === 2) &&
                                    <button
                                        onClick={() => handleKickUserButton(user.id)}
                                    >
                                        Kicka Partecipante <FontAwesomeIcon icon={faSnowplow} />
                                    </button>
                                }
                                {(user.role === 1 || user.role === 2) &&(
                                    utentiSilenziati.includes(user.id) ? (
                                        <button
                                            onClick={() => handleUnMuteUserButton(user.id)}
                                        >
                                            Smuta Partecipante <FontAwesomeIcon icon={faMicrophoneSlash} />
                                        </button>
                                    ) : (
                                        <button onClick={() => handleSilenziaUserButton(user.id)}>Silenzia Partecipante <FontAwesomeIcon icon={faMicrophone} /></button>
                                    )
                                )}
                                {(user.role === 1 || user.role === 2) &&(
                                    <button
                                        onClick={() => handleBanUserButton(user.id)}
                                    >
                                        Banna Partecipante <FontAwesomeIcon icon={faBan} />
                                    </button>
                                )}
                                {user.role === 2 &&(
                                    statoPartecipazione &&
                                    statoPartecipazione.value &&
                                    Array.isArray(statoPartecipazione.value) &&
                                    statoPartecipazione.value.find((partecipazione) => partecipazione.utente.id === user.id &&
                                        partecipazione.ruolo.nome === 'Organizzatore') ? (
                                        <button
                                            onClick={() => handleDeclassifyButton(user.id)}
                                        >
                                            Declassa <FontAwesomeIcon icon={faArrowTrendDown} />
                                        </button>
                                    ) : (
                                        <button
                                            onClick={() => handlePromotionButton(user.id)}
                                        >
                                            Promuovi <FontAwesomeIcon icon={faArrowTrendUp} />
                                        </button>
                                    ))
                                }
                            </div>
                        </>
                </div>
            ))}
            {showChangeNameModal && (
                <div className="modal" style={{ zIndex: "9" }}>
                    <div className="modal-content">
                        <span
                            className={"close"}
                            onClick={handleCloseChangeNamePopUp}
                        >&times;</span>
                        <label>
                            Nuovo Nome:
                            <input
                                type="text"
                                value={newName}
                                onChange={(e) => setNewName(e.target.value)}
                            />
                        </label>
                        <button onClick={handleChangeName}>Conferma</button>
                        {errore && <p style={{ color: 'red' }}>{errore}</p>}
                    </div>
                </div>
            )}
            {showSuccessPopup && (
                <div className={"modal"} style={{ zIndex: "9" }}>
                    <div className={"modal-content"}>
                        <span
                            className={"close"}
                            onClick={handleCloseSuccesPopUp}
                        >&times;</span>
                        <h3>{message}</h3>
                    </div>
                </div>
            )}
        </div>

    );
};

export default UserListInRoom;
