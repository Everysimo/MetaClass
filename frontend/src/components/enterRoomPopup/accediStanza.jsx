import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './accediStanza.css';

const AccediStanza = () => {
    const [showModal, setShowModal] = useState(false);
    const [codice, setCodice] = useState('');
    const [isErrorPopupVisible, setIsErrorPopupVisible] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [formReset, setFormReset] = useState(false);

    const handleShowModal = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setFormReset(true);
    };

    const handleCodeChange = (e) => {
        setCodice(e.target.value);
    };

    const sendDataToServer = async () => {
        if (codice.length !== 6) {
            setIsErrorPopupVisible(true);
            setErrorMessage('Il codice stanza deve essere di 6 cifre');
            return;
        }

        const dataToSend = {
            codice,
        };

        try {
            const response = await axios.post('http://localhost:8080/accessoStanza', dataToSend);
            console.log('Risposta dal server:', response.data);
        } catch (error) {
            console.error('ERRORE:', error);
        }
        setFormReset(false);
        setCodice('');
        setIsErrorPopupVisible(false);
    };

    useEffect(() => {
        if (formReset) {
            setCodice('');
            setIsErrorPopupVisible(false);
            setErrorMessage('');
            setFormReset(false);
        }
    }, [formReset]);

    return (
        <>
            <button onClick={handleShowModal}>Accedi</button>
            {showModal && (
                <div className={'modal'}>
                    <div className="modal-content">
                        <span className="close" onClick={handleCloseModal}>&times;</span>
                        <label>
                            insert room code:
                            <input
                                type="number"
                                placeholder={'Codice di 6 cifre'}
                                value={codice}
                                onChange={handleCodeChange}
                                maxLength={6}
                                minLength={6}
                                min={0}
                            />
                        </label>
                        <button onClick={() => setCodice('')}>Cancella</button>
                        <button onClick={sendDataToServer}>Invia</button>
                    </div>
                </div>
            )}
        </>
    );
};

export default AccediStanza;

