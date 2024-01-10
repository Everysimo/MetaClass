import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../PopUpStyles.css';

const AccediStanza = () => {
    const [showModal, setShowModal] = useState(false);
    const [codice, setCodice] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [formReset, setFormReset] = useState(false);
    const [displayOKButton, setDisplayOKButton] = useState(false);

    const handleShowModal = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        resetForm();
    };

    const handleCodeChange = (e) => {
        setCodice(e.target.value);
        setErrorMessage('');
        setDisplayOKButton(false);
    };

    const resetForm = () => {
        setCodice('');
        setErrorMessage('');
        setFormReset(true);
        setDisplayOKButton(false);
    };

    const sendDataToServer = async () => {
        if (codice.length !== 6) {
            setErrorMessage('Il codice stanza deve essere di 6 cifre');
            setDisplayOKButton(true);
            return;
        }

        const dataToSend = {
            codice,
        };

        try {
            const response = await axios.post('http://localhost:8080/accessoStanza', dataToSend);
            console.log('Risposta dal server:', response.data);
            setShowModal(false);
            resetForm();
        } catch (error) {
            console.error('ERRORE:', error);
            setErrorMessage('Errore durante l\'invio dei dati');
            setDisplayOKButton(true);
        }
    };

    useEffect(() => {
        if (formReset) {
            setCodice('');
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
                        {errorMessage && <p>{errorMessage}</p>}
                        {displayOKButton && <button onClick={resetForm}>OK</button>}
                        {!displayOKButton && <button onClick={() => setCodice('')}>Cancella</button>}
                        {!displayOKButton && <button onClick={sendDataToServer}>Invia</button>}
                    </div>
                </div>
            )}
        </>
    );
};

export default AccediStanza;
