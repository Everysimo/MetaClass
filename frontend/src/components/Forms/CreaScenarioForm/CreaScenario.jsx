import React, { useState } from 'react';
import '../PopUpStyles.css';
import { wait } from "@testing-library/user-event/dist/utils";

const CreaScenario = (props) => {
    const [showCreateFormModal, setShowCreateFormModal] = useState(false);

    const [state, setState] = useState({
        nome: "",
        descrizione: "",
        imageUrl: "",
        categoria: 0,
        isVisible: true,
        isErrorPopupVisible: false,
        errorMessage: "",
    });
    const handleShowCreateForm = () => {
        setShowCreateFormModal(true);
    };

    const handleCloseCreateFormModal = () => {
        setShowCreateFormModal(false);
    };

    const handleNameChange = (e) => {
        const inputValue = e.target.value;
        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        setState({ ...state, nome: capitalizedInput, error: null });
    };

    const handleDescChange = (e) => {
        const inputValue = e.target.value;
        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        setState({ ...state, descrizione: capitalizedInput, error: null });
    };

    const handleImmageChange = (e) => {
        setState({ ...state, imageUrl: e.target.value });
    };

    const handleCateChange = (e) => {
        setState({ ...state, categoria: e.target.value });
    };

    const handleErrorPopupClose = () => {
        setState({
            ...state,
            isErrorPopupVisible: false,
            errorMessage: "",
        });
    };

    const sendDataToServer = async () => {
        const { nome, descrizione, imageUrl, categoria } = state;

        if (categoria < 0) {
            setState({
                ...state,
                isErrorPopupVisible: true,
                errorMessage: "L'id Categoria deve essere maggiore o uguale a 0",
            });
            return;
        }

        const dataToSend = {
            nome,
            descrizione,
            imageUrl,
            categoria,
        };

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify(dataToSend),
        };

        try {
            console.log("la stringa json:", JSON.stringify(dataToSend));
            const response = await fetch('http://localhost:8080/admin/updateScenario', requestOption);
            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    const callFunction = () => {
        if (state.nome.trim() === '' || state.nome.length < 2) {
            setState({ ...state, error: 'Il campo nome non può essere vuoto o minore di 2 caratteri' });
            return;
        }

        if (state.descrizione.trim() === '') {
            setState({ ...state, error: 'Il campo descrizione non può essere vuoto' });
            return;
        } else if (state.descrizione.length < 2 || state.descrizione.length > 254) {
            setState({ ...state, error: 'Lunghezza descrizione errata' });
            return;
        } else if (!isNaN(state.descrizione.charAt(0))) {
            setState({ ...state, error: 'Errore durante la richiesta, formato Nome errato' });
            return;
        }

        setState({ ...state, error: null });

        sendDataToServer();
        console.log("dati del form", state);
        wait(100);
        handleClear();
    };

    const handleClear = () => {
        setState({
            ...state,
            nome: '',
            descrizione: '',
            imageUrl: '',
            categoria: 0,
        });
    };

    const handleClose = () => {
        setState({ ...state, isVisible: false });
        if (props.onClose) {
            props.onClose();
        }
    };

    const renderErrorPopup = () => {
        return (
            <div className={`error-popup ${state.isErrorPopupVisible ? '' : 'hidden'}`}>
                {state.errorMessage}
                <button onClick={handleErrorPopupClose}>Chiudi</button>
            </div>
        );
    };

    return (
        <div className={"modal"}>
            <div className={`modal-content ${state.isVisible ? '' : 'hidden'}`}>
                <span className="close" onClick={handleClose}>
                    &times;
                </span>
                <div className="card-content">
                    <label>
                        Nome:
                        <input
                            type="text"
                            name="nome"
                            value={state.nome}
                            onChange={handleNameChange}
                        />
                    </label>
                    <label>
                        Descrizione:
                        <input
                            type="text"
                            name="descrizione"
                            value={state.descrizione}
                            onChange={handleDescChange}
                        />
                    </label>
                    <label>
                        Immagine:
                        <input
                            type="text"
                            name="immagine"
                            value={state.imageUrl}
                            onChange={handleImmageChange}
                        />
                    </label>
                    <label>
                        Id Categoria:
                        <input
                            type="number"
                            name="idCategoria"
                            value={state.categoria}
                            onChange={handleCateChange}
                            min={0}
                        />
                    </label>
                    <button onClick={handleClear}>Cancella</button>
                    <button onClick={callFunction}>Invia</button>
                    {state.error && <p style={{ color: 'red' }}>{state.error}</p>}
                </div>
            </div>
        </div>
    );
};

export default CreaScenario;

