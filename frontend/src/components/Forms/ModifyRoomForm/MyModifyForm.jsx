import React, {useEffect, useState} from 'react';
import '../PopUpStyles.css';
import {useParams} from "react-router-dom";

const MyModifyForm = () => {
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({});
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [formReset, setFormReset] = useState(false); // State for form reset
    const { id: id_stanza } = useParams();

    //si usa useParams per farsi passare il parametro
    const [state, setState] = useState({
        nome: "",
        descrizione: "",
        tipoAccesso: '',
        max_Posti: '',
    });

    useEffect(() => {
        if (formReset) {
            setFormData({}); // Reset form data
            setErrorMessage(''); // Reset error message
            setSuccessMessage(''); // Reset success message
            setFormReset(false); // Reset formReset state
        }
    }, [formReset]);
    const handleCloseModal = () => {
        setShowModal(false);
        setFormReset(true); // Set form reset to true when modal is closed
    };


    const handleNameChange = (e) => {
        setState({ ...state, nome: e.target.value });
    };

    const handleDescriptionChange = (e) => {
        setState({ ...state, descrizione: e.target.value });
    };

    const handleOptionChange = (e) => {
        setState({ ...state, tipoAccesso: e.target.value });
    };

    const handleMAXChange = (e) => {
        setState({ ...state, max_Posti: parseInt(e.target.value, 10) });
    };

    /*funzioni per eliminare la stanza*/
    const deleteButton = () => {
        console.log("sono nel button delete");
        handleDeleteRoom();
    };

    const handleDeleteRoom = async () => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };

        try {
            const response = await fetch(`http://localhost:8080/eliminaStanza/${id_stanza}`, requestOption);
            const responseData = await response.json();
            console.log("Risposta del server:", responseData);
            setSuccessMessage(responseData.message);
        } catch (error) {
            console.error('Errore:', error);
        }
    };

    const sendDataModifyRoom = async () => {

        const {nome, descrizione, tipoAccesso, max_Posti} = state;
        const dataToSend = {};

        // Aggiungi solo i campi non vuoti all'oggetto dataToSend
        if (nome) {
            dataToSend.nome = nome;
        }
        if (descrizione) {
            dataToSend.descrizione = descrizione;
        }
        if (tipoAccesso) {
            dataToSend.tipoAccesso = tipoAccesso;
        }
        if (max_Posti) {
            dataToSend.max_Posti = max_Posti;
        }

        // Verifica se l'oggetto dataToSend è vuoto
        if (Object.keys(dataToSend).length === 0) {
            setErrorMessage('Nessun dato da inviare.');
            return;
        }

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify(dataToSend)
        };

        try {
            console.log("la stringa json:", dataToSend);

            const response = await fetch(`http://localhost:8080/modifyRoomData/${id_stanza}`, requestOption);

            const responseData = await response.json();
            setSuccessMessage(responseData.message);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    }

    return (
        <>
            <button onClick={() => setShowModal(true)}>Modifica stanza</button>
            {showModal && (
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={handleCloseModal}>&times;</span>
                        {successMessage ? (
                            <p >{successMessage}</p>
                        ) : (
                        <div className={"childDiv"}>
                            <p className={'textp'}>Inserisci Nuovo Nome:</p>
                            <input
                                placeholder={"Aggiungi Nuovo Nome"}
                                type="text"
                                value={state.nome}
                                onChange={handleNameChange}
                            />

                            <div className={'textarea-box'}>
                                <p className={'textp'}>Inserisci Nuova Descrizione:</p>
                                <textarea
                                    className={'textarea-field'}
                                    placeholder={'Aggiungi Descrizione'}
                                    rows={5}
                                    type="text"
                                    value={state.descrizione}
                                    onChange={handleDescriptionChange}
                                />
                            </div>

                            <p className={'textp'}>Scegli: pubblica o privata:</p>
                            <div className="select">
                                <select value={state.tipoAccesso} onChange={handleOptionChange}>
                                    <option value=""> -Scegli un'opzione-</option>
                                    <option value="false">Pubblica</option>
                                    <option value="true">Privata</option>
                                </select>
                            </div>

                            <p className={'textp'}>Inserisci numero MAX utenti:</p>
                            <input
                                placeholder={'MAX Posti'}
                                type="number"
                                min="1" max="999"
                                value={state.maxPosti}
                                onChange={handleMAXChange}
                            />
                            <button
                                type="button"
                                onClick={deleteButton}
                            >
                                Elimina stanza
                            </button>
                            <button
                                type="button"
                                onClick={() => sendDataModifyRoom()}
                            >
                                Salva
                            </button>
                            {errorMessage &&
                                <p className={"errorMsg"}>{errorMessage}</p>
                            }
                        </div>)}
                    </div>
                </div>
            )}
        </>
    );
};
export default MyModifyForm;