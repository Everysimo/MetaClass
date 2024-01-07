import React, {Component} from 'react'
import './accediStanza.css'
import {wait} from "@testing-library/user-event/dist/utils";
export default class AccediStanza extends Component{
    //stato di partenza dei parametri
    state = {
        codice: "",
        //tipoAccesso: false,
        isVisible: true,
    };
    //funzione similar costruttore per settare i valori
    responseForm = response => {
        this.setState({
            codice: response.codice,
            //tipoAccesso: response.tipoAccesso,
            isVisible: response.isVisible,
        });
    };
//le varie handle richiamate quando passo i valori nelle input form

    handleCodeChange = (e) => {
        this.setState({codice: e.target.value})
    };

    /*handleOptionChange = (e) => {
        this.setState({tipoAccesso: e.target.value})
    };*/

    //simposta invibile il div
    handleClose = () => {
        // Nascondi la card impostando isVisible su false
        this.setState({isVisible: false});
    };
    handleClear = () => {
        this.setState({codice: ('')});
    };

    /*funzione per inviare i parametri a crea stanza*/

    sendDataToServer = async() =>{
        const {codice/*, tipoAccesso*/} = this.state;
        const dataToSend = {
            codice,
            //tipoAccesso,

        };

        const requestOption = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dataToSend)
        };

        try{
            console.log("la stringa json:", JSON.stringify(dataToSend));
            //col fetch faccio la richiesta, al URL descritto
            const response = await fetch('http://localhost:8080/accessoStanza', requestOption);
            //con la await attento la risposta dal fetch
            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        }catch(error){
            console.error('ERRORE:', error);
        }


    }

    callFunction = () => {
        if (this.state.codice.length !== 6) {
            this.setState({
                isErrorPopupVisible: true,
                errorMessage: "Il codice stanza deve essere di 6 cifre",
            });
        }
        this.sendDataToServer();
        console.log("dati del form", this.state)
        wait(100);
        this.handleClear();
    }
    handleErrorPopupClose = () => {
        this.setState({
            isErrorPopupVisible: false,
            errorMessage: "",
        });
    };

    renderErrorPopup = () => {
        return (
            <div className={`error-popup ${this.state.isErrorPopupVisible ? '' : 'hidden'}`}>
                {this.state.errorMessage}
                <button  onClick={this.handleErrorPopupClose}>Chiudi</button>
            </div>
        );
    };

    render(){
        return (
            <div>
                {this.renderErrorPopup()}
                <div className={`card ${this.state.isVisible ? '' : 'hidden'}`}>
                    <button className="close-button" onClick={this.handleClose}>
                        X
                    </button>
                    <div className="card-content">
                        <label>
                            insert room code:
                            <input
                                type="number"
                                placeholder={'Codice di 6 cifre'}
                                value={this.state.codice}
                                onChange={this.handleCodeChange}
                                maxLength={6}
                                minLength={6}
                                min={0}
                            />
                        </label>
                        <div className="button-container">
                            <button onClick={this.handleClear}>Cancella</button>
                            <button onClick={() =>  this.callFunction()}>Invia</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    };
}
