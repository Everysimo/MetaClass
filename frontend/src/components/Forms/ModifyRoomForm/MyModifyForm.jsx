import '../PopUpStyles.css';
import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";

const MyModifyForm = () => {
    const [showModal, setShowModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const { id: id_stanza } = useParams();

    //si usa useParams per farsi passare il parametro
    const [state, setState] = useState({
        nome: "",
        descrizione: "",
        tipoAccesso: "",
        maxPosti: "",
    });

    const handleCloseModal = () => {
        setShowModal(false);
        setErrorMessage('');
        if(successMessage){
            setSuccessMessage('');
            window.location.reload();
        }
        setState({
            nome: "",
            descrizione: "",
            tipoAccesso: "",
            maxPosti: "",
        });
    };

    const handleNameChange = (e) => {
        if(
            e.target.value !== '' &&
            !(e.target.value.charAt(0) === e.target.value.charAt(0).toUpperCase()))
                setErrorMessage('Il nome della stanza deve cominciare con la lettera maiuscola');
        else{
            setErrorMessage('');
            setState({ ...state, nome: e.target.value });
        }
    };

    const handleDescriptionChange = (e) => {
        if(
            e.target.value !== '' &&
            !(e.target.value.charAt(0) === e.target.value.charAt(0).toUpperCase()))
            setErrorMessage('La descrizione deve cominciare con la lettera maiuscola');
        else{
            setErrorMessage('');
            setState({ ...state, descrizione: e.target.value });
        }
    };

    const handleOptionChange = (e) => {
        setState({ ...state, tipoAccesso: e.target.value });
    };

    const handleMAXChange = (e) => {
        setState({ ...state, maxPosti: parseInt(e.target.value, 10) });
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

        const {nome, descrizione, tipoAccesso, maxPosti} = state;
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
        if (maxPosti) {
            dataToSend.maxPosti = maxPosti;
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
                            <>
                                <div className={"masterDiv"}>
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
                                    </div>
                                    <div className={"childDiv"}>
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
                                    </div>
                                </div>
                                <div className={"childDiv"}>
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
                                </div>
                                {errorMessage &&
                                    <p className={"errorMsg"}>{errorMessage}</p>
                                }
                            </>
                        )}
                    </div>
                </div>
            )}
        </>
    );
};
export default MyModifyForm;