import React, { Component } from 'react';
import {faPlay} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default class AvviaMeeting extends Component {
    state = {
        id_meeting: this.props.id_meeting || "", // Imposta il valore iniziale con quello ricevuto come prop
        isVisible: true,
        isErrorPopupVisible: false,
        errorMessage: "",
    };

    sendDataToServer = async () => {
        const { id_meeting} = this.state;

        // Validazione: Assicurati che idCategoria sia >= 0

        const dataToSend = {
            id_meeting,
        };

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify(dataToSend)
        };

        try{
            console.log("la stringa json:", JSON.stringify(dataToSend));
            const response = await fetch(`http://localhost:8080/avviaMeeting/${id_meeting}`, requestOption);
            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
            if (responseData && responseData.value) {
                console.log(responseData.message);
            }
        }
        catch (error) {
            console.error('ERRORE:', error);
        }
    };

    callFunction = () => {
        // Invia i dati al server utilizzando this.state.id_meeting
        this.sendDataToServer();
        console.log("dati del form", this.state);

    };
    render() {
        return (
            <div className="button-container">
                <button onClick={() => this.callFunction()}> Avvia Meeting <FontAwesomeIcon icon={faPlay} size="xl" style={{color: "#ffffff",}}/> </button>
            </div>
        );
    };
}
