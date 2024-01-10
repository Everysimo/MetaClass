import React, { Component } from 'react';
import '../Forms/CreaScenarioForm/creaScenario.css';

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
                Authorization: 'Bearer ' + sessionStorage.getItem('token')
            },
            body: JSON.stringify(dataToSend)
        };

        try {
            const response = await fetch(`http://localhost:8080/avviaMeeting/${id_meeting}`, requestOption);

            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    callFunction = () => {
        // Invia i dati al server utilizzando this.state.id_meeting
        this.sendDataToServer();
        console.log("dati del form", this.state);

    };

    handleClose = () => {
        // Nascondi la card impostando isVisible su false
        this.setState({ isVisible: false });

        // Chiama la funzione di chiusura ricevuta come prop
        if (this.props.onClose) {
            this.props.onClose();
        }
    };


    render() {
        return (
            <div>
                <div className={`card ${this.state.isVisible ? '' : 'hidden'}`}>
                    <button className="close-button" onClick={this.handleClose}>
                        X
                    </button>
                    <div className="card-content">
                        <div className="button-container">
                            <button onClick={() => this.callFunction()}>Avvia Meeting</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    };
}
