import React, { useState, useEffect } from 'react';
import '../PopUpStyles.css';
import {faCheck, faFileCircleXmark} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const Questionario = (props) => {
    //const [valutazione, setValutazione] = useState(0);
    const [errore, setErrore] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showSuccessPopup, setShowSuccessPopup] = useState(false);
    const [id_meeting, setId] = useState(props.id_meeting);

    const [state, setState] = useState({
        immersionLevel: "",
        motionSickness: "",
    });

    useEffect(() => {
        if (showSuccessPopup) {
        }
    }, [showSuccessPopup]);


    const handleImmersionChange = (e) => {
        const newValue = parseInt(e.target.value, 10);
        setState(prevState => ({ ...prevState, immersionLevel: newValue }));
        console.log("dato di immersione:", newValue);
    };

    const handleSicknessChange = (e) => {
        const newValue = parseInt(e.target.value, 10);
        setState(prevState => ({ ...prevState, motionSickness: newValue }));
        console.log("dato di sickness:", newValue);
    };


    const sendDataToServer = async () => {

        const { immersionLevel, motionSickness,  } = state;

        const dataToSend = {immersionLevel, motionSickness}

        console.log("dei dati:", dataToSend);

        if (errore) {
            console.error('Valutazione non valida:', errore);
        }

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify({ dataToSend }),
        };

        try {
            console.log("data to send", dataToSend)
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
        /*console.log("hai schiacciato");
        if (valutazione < 1 || valutazione > 5) {
            setErrore('Il valore deve essere compreso tra 1 e 5');
        } else {
            setErrore(null); // Azzera l'errore se il valore è valido
            sendDataToServer();
        }*/
        sendDataToServer()
    };

    const handleShow = () => {
        setShowModal(true);
    };

    const handleClose = () => {
        setShowModal(false);
    };

    const handleCloseSuccessPopup = () => {
        setTimeout(() => {
            setShowSuccessPopup(false);
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
                        <p style={{fontSize: "14px", textAlign: "center"}}>Livello di immersività (1 a 5)</p>
                        <div className="dots-container">
                            {[1, 2, 3, 4, 5].map((value) => (
                                <label key={value} className={`dot ${value === state.immersionLevel ? 'active' : ''}`}>
                                    <input
                                        type="radio"
                                        name="immersionLevel"
                                        value={value}
                                        onChange={handleImmersionChange}
                                    />
                                </label>
                            ))}
                        </div>
                        <p style={{fontSize: "14px", textAlign: "center"}}>Livello motion sickness (1 a 5)</p>
                        <div className="dots-container">
                            {[1, 2, 3, 4, 5].map((value) => (
                                <label key={value} className={`dot ${value === state.motionSickness ? 'active' : ''}`}>
                                    <input
                                        type="radio"
                                        name="motionSickness"
                                        value={value}
                                        onChange={handleSicknessChange}
                                    />
                                </label>
                            ))}
                        </div>
                        <button onClick={handleSubmit}>Compila</button>
                        {errore && <p style={{color: 'red'}}>{errore}</p>}
                    </div>
                </div>
            }
            {showSuccessPopup && (
                <div className={"modal"}>
                    <div className={"modal-content"}>
                        <span
                            className={"close"}
                            onClick={handleCloseSuccessPopup}
                        >&times;</span>
                        <p>Questionario compilato con successo <FontAwesomeIcon icon={faCheck} size="2xl" style={{color: "#63E6BE",}} /></p>
                    </div>
                </div>
            )}
        </>
    );
};

export default Questionario;
