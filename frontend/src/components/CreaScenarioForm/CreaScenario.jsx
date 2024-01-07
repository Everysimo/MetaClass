import React, { Component } from 'react';
import './creaScenario.css';
import { wait } from "@testing-library/user-event/dist/utils";

export default class CreaScenario extends Component {
    state = {
        nome: "",
        descrizione: "",
        imageUrl: "",
        categoria: 0,
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
        const { nome, descrizione, imageUrl, categoria } = this.state;

        // Validazione: Assicurati che idCategoria sia >= 0
        if (categoria < 0) {
            this.setState({
                isErrorPopupVisible: true,
                errorMessage: "L'id Categoria deve essere maggiore o uguale a 0",
            });
            return;
        }

        const dataToSend = {
            nome,
            descrizione,
            imageUrl,
            categoria,
        };

        const requestOption = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dataToSend),
        };

        try {
            console.log("la stringa json:", JSON.stringify(dataToSend));
            const response = await fetch('http://localhost:8080/updateScenario', requestOption);
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

    handleClear = () => {
        this.setState({
            nome: '',
            descrizione: '',
            imageUrl: '',
            categoria: 0,
        });
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
                        <label>
                            Descrizione:
                            <input
                                type="text"
                                name="descrizione"
                                value={this.state.descrizione}
                                onChange={this.handleInputChange}
                            />
                        </label>
                        <label>
                            Immagine:
                            <input
                                type="text"
                                name="immagine"
                                value={this.state.imageUrl}
                                onChange={this.handleInputChange}
                            />
                        </label>
                        <label>
                            Id Categoria:
                            <input
                                type="number"
                                name="idCategoria"
                                value={this.state.categoria}
                                onChange={this.handleInputChange}
                                min={0}
                            />
                        </label>
                        <div className="button-container">
                            <button onClick={this.handleClear}>Cancella</button>
                            <button onClick={() => this.callFunction()}>Invia</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    };
}
