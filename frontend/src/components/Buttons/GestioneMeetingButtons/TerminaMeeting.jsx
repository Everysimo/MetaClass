import React, { Component } from 'react';
import {faPlay } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export default class TerminaMeeting extends Component {
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
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify(dataToSend)
        };

        try {
            const response = await fetch(`http://localhost:8080/terminaMeeting/${id_meeting}`, requestOption);
            const responseData = await response.json();

            if (responseData && responseData.value) {
                console.log(responseData.message);
                // Dopo aver terminato il meeting con successo, aggiorna la pagina
                window.location.reload();
            }
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    callFunction = () => {
        this.sendDataToServer();
    };

    render() {
        return (
            <div className="button-container">
                <button onClick={this.callFunction}> Termina Meeting <FontAwesomeIcon icon={faPlay} size="xl" style={{ color: "#ffffff" }} /> </button>
            </div>
        );
    };
}
