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

    handleNameChange = (e) => {
        const inputValue = e.target.value;

        //prima lettera grande
        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        this.setState({ nome: capitalizedInput, error: null });

    };

    handleDescChange = (e) => {
        const inputValue = e.target.value;

        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        this.setState({ descrizione: capitalizedInput, error: null });
    };
    handleImmageChange = (e) => {
        this.setState({ imageUrl: e.target.value });
    };
    handleCateChange = (e) => {
        this.setState({ categoria: e.target.value });
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
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify(dataToSend),
        };

        try {
            console.log("la stringa json:", JSON.stringify(dataToSend));
            const response = await fetch('http://localhost:8080/admin/updateScenario', requestOption);
            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    callFunction = () => {

        //CONTROLLI DA TRASFORMARE IN POP UP
        if (this.state.nome.trim() === '' || this.state.nome.length < 2) {
            this.setState({ error: 'Il campo nome non può essere vuoto o minore di 2 caratteri' });
            return;
        }

        if (this.state.descrizione.trim() === '') {
            this.setState({error: 'Il campo descrizione non può essere vuoto'});
            return;
        }else if(this.state.descrizione.length < 2 || this.state.descrizione.length > 254) {
            this.setState({error: 'Lunghezza descrizione errata'});
            return;
        }else if(!isNaN(this.state.descrizione.charAt(0))) {
            this.setState({ error: 'Errore durante la richiesta, formato Nome errato' });
            return;
        }


        this.setState({ error: null });

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
    handleClose = () => {
        // Nascondi la card impostando isVisible su false
        this.setState({ isVisible: false });

        // Chiama la funzione di chiusura ricevuta come prop
        if (this.props.onClose) {
            this.props.onClose();
        }
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
                                onChange={this.handleNameChange}
                            />
                        </label>
                        <label>
                            Descrizione:
                            <input
                                type="text"
                                name="descrizione"
                                value={this.state.descrizione}
                                onChange={this.handleDescChange}
                            />
                        </label>
                        <label>
                            Immagine:
                            <input
                                type="text"
                                name="immagine"
                                value={this.state.imageUrl}
                                onChange={this.handleImmageChange}
                            />
                        </label>
                        <label>
                            Id Categoria:
                            <input
                                type="number"
                                name="idCategoria"
                                value={this.state.categoria}
                                onChange={this.handleCateChange}
                                min={0}
                            />
                        </label>
                        <div className="button-container">
                            <button onClick={this.handleClear}>Cancella</button>
                            <button onClick={() => this.callFunction()}>Invia</button>
                        </div>
                        {this.state.error && <p style={{ color: 'red' }}>{this.state.error}</p>}

                    </div>
                </div>
            </div>
        );
    };
}
