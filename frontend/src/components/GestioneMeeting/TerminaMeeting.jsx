import React, { Component } from 'react';
import '../CreaScenarioForm/creaScenario.css';

export default class AvviaMeeting extends Component {
    state = {
        id_meeting: this.props.id_meeting || "",
        isVisible: true,
        isErrorPopupVisible: false,
        errorMessage: "",
    };

    sendDataToServer = async () => {
        const { id_meeting } = this.state;

        const dataToSend = {
            id_meeting,
        };

        const requestOption = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dataToSend),
        };

        try {
            // Utilizza il valore dinamico di id_meeting
            const response = await fetch(`http://localhost:8080/avviaMeeting/${id_meeting}`, requestOption);

            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    callFunction = () => {
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
                            <button onClick={() => this.callFunction()}>Termina Meeting</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    };
}
