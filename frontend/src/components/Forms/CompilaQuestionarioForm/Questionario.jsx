import React, { useState, useEffect } from 'react';
import '../PopUpStyles.css';
import './Questionario.css';
import {faCheck, faFileCircleXmark, faInfo} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import InfoPopUp from "../infoPopUp";

const Questionario = (props) => {
    const [errore, setErrore] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showSuccessPopup, setShowSuccessPopup] = useState(false);
    const id_meeting = props.id_meeting;
    const infoPopup = InfoPopUp();

    const [state, setState] = useState({
        immersion: "",
        motion: "",
    });

    const handleImmersionChange = (e) => {
        const newValue = parseInt(e.target.value, 10);
        setState(prevState => ({ ...prevState, immersion: newValue }));
        console.log("dato di immersione:", newValue);
    };

    const handleSicknessChange = (e) => {
        const newValue = parseInt(e.target.value, 10);
        setState(prevState => ({ ...prevState, motion: newValue }));
        console.log("dato di sickness:", newValue);
    };

    const sendDataToServer = async () => {

        const { immersion, motion,  } = state;

        const immersionLevel = parseInt(immersion, 10)
        const motionSickness = parseInt(motion, 10)
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
            body: JSON.stringify( dataToSend ),
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
        console.log("----", state.immersion, state.motion)
        if (state.immersion === '' || state.motion === ''){
            setErrore('I campi non possono essere vuoti.');
            return;
        }
        else{
            sendDataToServer();
        }
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
                    <div
                        className={"modal-content"}
                    >
                        <span
                            className={"close"}
                            onClick={handleClose}
                        >&times;</span>
                        <h2>
                            Compila il Questionario
                            <div
                                style={{
                                    height: "auto",
                                    width: "20px",
                                    cursor: "pointer",
                                    marginInline: "auto"
                                }}
                                onMouseOver={infoPopup.handleInfoMouseOver}
                                onMouseLeave={infoPopup.handleInfoMouseLeave}
                            >
                                <FontAwesomeIcon
                                    className={"infoIcon"}
                                    icon={faInfo}
                                    size="xs"
                                    style={{
                                        color: "#74C0FC",
                                        cursor: "pointer"
                                    }}
                                />
                            </div>
                        </h2>
                        {infoPopup.popup}
                        <p style={{fontSize: "14px", textAlign: "center"}}>Livello di immersività (1 a 5)</p>
                        <div className="dots-container">
                            {[1, 2, 3, 4, 5].map((value) => (
                                <label key={value} className={`dot ${value === state.immersion ? 'active' : ''}`}>
                                    <input
                                        type="radio"
                                        name="immersion"
                                        value={value}
                                        onChange={handleImmersionChange}
                                    />
                                </label>
                            ))}
                        </div>
                        <p style={{fontSize: "14px", textAlign: "center"}}>Livello motion sickness (1 a 10)</p>
                        <div className="dots-container">
                            {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((value) => (
                                <label key={value} className={`dot ${value === state.motion ? 'active' : ''}`}>
                                    <input
                                        type="radio"
                                        name="motion"
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
