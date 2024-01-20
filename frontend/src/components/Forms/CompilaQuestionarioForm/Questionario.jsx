import React, { useState, useEffect } from 'react';
import { faFileCircleXmark } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const Questionario = (props) => {
    const [valutazioneString, setValutazione] = useState("");
    const [errore, setErrore] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showSuccessPopup, setShowSuccessPopup] = useState(false);
    const [id_meeting, setId] = useState(props.id_meeting);

    useEffect(() => {
        if (showSuccessPopup) {
        }
    }, [showSuccessPopup]);


    const handleValutazioneChange = (e) => {
        setValutazione(e.target.value);
    };

    const sendDataToServer = async () => {
        if (errore) {
            console.error('Valutazione non valida:', errore);
        }

        const valutazione = parseInt(valutazioneString, 10);
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify({ valutazione }),
        };

        try {
            const response = await fetch(`http://localhost:8080/compilaQuestionario/${id_meeting}`, requestOption);
            const responseData = await response.json();

            console.log("Risposta dal server:", responseData);

            if (response.ok) {
                console.log('Valutazione inviata con successo!');
                // Chiudi il modal
                handleClose();
                // Mostra il pop-up di successo
                setShowSuccessPopup(true);



            } else {
                console.error('Errore durante l\'invio della valutazione al backend');
            }
        } catch (error) {
            console.error('Errore nella richiesta fetch:', error);
        }
    };

    const handleSubmit = () => {
        console.log("hai schiacciato");
        if (valutazioneString < 1 || valutazioneString > 5) {
            setErrore('Il valore deve essere compreso tra 1 e 5');
        } else {
            setErrore(null); // Azzera l'errore se il valore è valido
            sendDataToServer();
        }
    };

    const handleShow = () => {
        setShowModal(true);
    };

    const handleClose = () => {
        setShowModal(false);
    };
    const handleCloseSuccesPopUp = () => {
        setTimeout(() => {
            // Simuliamo il reindirizzamento dopo 2 secondi
            setShowSuccessPopup(false);
            // Aggiungi le azioni specifiche per il reindirizzamento
            window.location.replace(window.location.pathname);
        }, 2000);
    };

    return (
        <>
            <FontAwesomeIcon
                icon={faFileCircleXmark}
                size="2xl"
                style={{ color: "#ff2600", cursor: "pointer" }}
                onClick={handleShow}
            />
            {showModal &&
                <div className={"modal"}>
                    <div className={"modal-content"}>
                        <span
                            className={"close"}
                            onClick={handleClose}
                        >&times;</span>
                        <h2>Compila il Questionario</h2>
                        <label>
                            Inserisci una valutazione da 1 a 5 inerente all'immersività nel meeting:
                            <input
                                type="number"
                                min="1"
                                max="5"
                                value={valutazioneString}
                                onChange={handleValutazioneChange}
                            />
                        </label>
                        <button onClick={handleSubmit}>Compila</button>
                        {errore && <p style={{ color: 'red' }}>{errore}</p>}
                    </div>
                </div>
            }
            {showSuccessPopup && (
                <div className={"modal"}>
                    <div className={"modal-content"}>
                        <span
                            className={"close"}
                            onClick={handleCloseSuccesPopUp}
                        >&times;</span>
                        <p>Questionario compilato con successo</p>
                    </div>
                </div>
            )}
        </>
    );
};

export default Questionario;
