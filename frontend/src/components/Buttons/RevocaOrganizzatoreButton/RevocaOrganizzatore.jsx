import React, { Component } from 'react';
import '../../Forms/CreaScenarioForm/creaScenario.css';
import { wait } from "@testing-library/user-event/dist/utils";

export default class RevocaOrganizatore extends Component {
    state = {
        nome: "",
        isVisible: true,
        isErrorPopupVisible: false,
        errorMessage: "",
    };

    handleInputChange = (e) => {
        const { name, value } = e.target;
        this.setState({ [name]: value });
    };

    handleErrorPopupClose = () => {
        this.setState({
            isErrorPopupVisible: false,
            errorMessage: "",
        });
    };

    sendDataToServer = async () => {
        const { nome} = this.state;

        // Validazione: Assicurati che idCategoria sia >= 0

        const dataToSend = {
            nome,
        };

        const requestOption = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dataToSend),
        };

        try {
            console.log("la stringa json:", JSON.stringify(dataToSend));
            const response = await fetch('http://localhost:8080/updateCategoria', requestOption);
            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    callFunction = () => {
        this.sendDataToServer();
        console.log("dati del form", this.state);
        wait(100);
        this.handleClear();
    };

    handleClose = () => {
        // Nascondi la card impostando isVisible su false
        this.setState({isVisible: false});
    };
    renderErrorPopup = () => {
        return (
            <div className={`error-popup ${this.state.isErrorPopupVisible ? '' : 'hidden'}`}>
                {this.state.errorMessage}
                <button onClick={this.handleErrorPopupClose}>Chiudi</button>
            </div>
        );
    };


    render() {
        return (
            <div>
                {this.renderErrorPopup()}
                <div className={`card ${this.state.isVisible ? '' : 'hidden'}`}>
                    <button className="close-button" onClick={this.handleClose}>
                        X
                    </button>
                    <div className="card-content">
                        <label>
                            Nome:
                            <input
                                type="text"
                                name="nome"
                                value={this.state.nome}
                                onChange={this.handleInputChange}
                            />
                        </label>
                        <div className="button-container">
                            <button onClick={() => this.callFunction()}>Conferma</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    };
}
