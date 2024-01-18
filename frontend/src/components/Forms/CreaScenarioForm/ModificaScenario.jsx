import React, { useState } from 'react';
import '../PopUpStyles.css';

const ModificaScenario = (props) => {
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [state, setState] = useState({
        nome: "",
        descrizione: "",
        imageUrl: "",
        categoria: 0,
        isVisible: true
    });

    const handleNameChange = (e) => {
        setState({ ...state, nome: e.target.value });
    };

    const handleDescChange = (e) => {
        setState({ ...state, descrizione: e.target.value });
    };

    const handleImmageChange = (e) => {
        setState({ ...state, imageUrl: e.target.value });
    };

    const handleCateChange = (e) => {
        setState({ ...state, categoria: e.target.value });
    };

    const sendDataToServer = async () => {
        const { nome, descrizione, imageUrl, categoria } = state;

        if (categoria < 0) {
            setErrorMessage("L'id Categoria deve essere maggiore o uguale a 0");
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
            setLoading(true);
            const response = await fetch('http://localhost:8080/admin/updateScenario', requestOption);
            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);

            if (response.ok) {
                setSuccessMessage(responseData.message);
            } else {
                setErrorMessage(responseData.message || 'Errore durante l\'operazione');
            }
        } catch (error) {
            console.error('ERRORE:', error);
            setErrorMessage('Errore durante la connessione al server');
        } finally {
            setLoading(false);
        }
    };

    const callFunction = () => {
        setSuccessMessage('');
        setErrorMessage('');
        sendDataToServer();
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

    return (
        <div className={"modal"}>
            <div className={`modal-content ${state.isVisible ? '' : 'hidden'}`}>
                <span className="close" onClick={handleClose}>
                    &times;
                </span>
                {loading ? (
                    <p>Loading...</p>
                ) : (
                    <>
                        {successMessage ? (
                            <p>{successMessage}</p>
                        ) : errorMessage ? (
                            <p>{errorMessage}</p>
                        ) : (
                            <>
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
                            </>
                        )}
                    </>
                )}
            </div>
        </div>
    );
};

export default ModificaScenario;