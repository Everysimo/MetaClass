import React, { useState } from 'react';
import '../PopUpStyles.css';
import { wait } from "@testing-library/user-event/dist/utils";
import {faCheck} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const CreaCategoria = (props) => {
    const [formData, setFormData] = useState({
        nome: "",
        descrizione: "",
    });

    const [isVisible, setIsVisible] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const handleNameChange = (e) => {
        setFormData({ ...formData, nome: e.target.value });
    };

    const handleDescChange = (e) => {
        setFormData({ ...formData, descrizione: e.target.value });
    };

    const sendDataToServer = async () => {
        const { nome, descrizione } = formData;

        if (nome.trim() === '' || descrizione.trim() === '') {
            setErrorMessage('I campi Nome e Descrizione non possono essere vuoti');
            return;
        }


        const dataToSend = {
            nome,
            descrizione
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
            console.log("la stringa json:", JSON.stringify(dataToSend));
            const response = await fetch('http://localhost:8080/admin/updateCategoria', requestOption);
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
        wait(100);
        handleClear();
    };

    const handleClear = () => {
        setFormData({
            nome: '',
            descrizione: '',
        });
    };

    const handleClose = () => {
        setIsVisible(false);

        if (props.onClose) {
            props.onClose();
        }
    };

    return (
        <div className={"modal"}>
            <div className={`modal-content ${isVisible ? '' : 'hidden'}`}>
                <span className="close" onClick={handleClose}>
                    &times;
                </span>
                <div className="card-content">
                    {loading ? (
                        <p>Loading...</p>
                    ) : (
                        <>
                            {successMessage ? (
                                <p>{successMessage} <FontAwesomeIcon icon={faCheck} size="2xl" style={{color: "#63E6BE",}} /></p>
                            ) : errorMessage ? (
                                <p>{errorMessage}</p>
                            ) : (
                                <>
                                    <label>
                                        Nome:
                                        <input
                                            type="text"
                                            name="nome"
                                            value={formData.nome}
                                            onChange={handleNameChange}
                                        />
                                    </label>
                                    <label>
                                        Descrizione:
                                        <input
                                            type="text"
                                            name="descrizione"
                                            value={formData.descrizione}
                                            onChange={handleDescChange}
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
        </div>
    );
};

export default CreaCategoria;