
import React, {Component} from 'react'
import "./MyCreateForm.css"
import {Divider} from "@chakra-ui/react";

export default class MyCreateForm extends Component{


    //stato di partenza dei parametri
    state = {
        nome: "",
        codiceStanza: "",
        descrizione: "",
        tipoAccesso: false,
        maxPosti: 0,
    };

    //funzione similar costruttore per settare i valori
    responseForm = response => {
        this.setState({
            nome: response.nome,
            codiceStanza: response.codiceStanza,
            descrizione: response.descrizione,
            tipoAccesso: response.tipoAccesso,
            maxPosti: response.maxPosti,
        });
    };

//le varie handle richiamate quando passo i valori nelle input form
    handleNameChange = (e) => {
        this.setState({nome: e.target.value});
    };

    handleCodeChange = (e) => {
        this.setState({codiceStanza: e.target.value})
    };

    handleDescriptionChange = (e) => {
        this.setState({descrizione: e.target.value})
    };

    handleOptionChange = (e) => {
        this.setState({tipoAccesso: e.target.value})
    };

    handleMAXChange = (e) => {
        this.setState({maxPosti: e.target.value})
    };


    /*funzione per inviare i parametri a crea stanza*/


    sendDataToServer = async() =>{
        const {nome, codiceStanza, descrizione, tipoAccesso, maxPosti} = this.state;
        const dataToSend = {
            nome,
            codiceStanza,
            descrizione,
            tipoAccesso,
            maxPosti
        };

        const requestOption = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dataToSend)
        };

        try{
                console.log("la stringa json:", JSON.stringify(dataToSend));
                    //col fetch faccio la richiesta, al URL descritto
                const response = await fetch('http://localhost:8080/creastanza', requestOption);
                    //con la await attento la risposta dal fetch
                const responseData = await response.json();
                console.log("Risposta dal server:", responseData);
        }catch(error){
            console.error('ERRORE:', error);
        }


    }

    callFunction = () => {

        this.sendDataToServer();
        console.log("dati del form", this.state)
    }
    render(){
    return(
        <div className={'primary'}>
            <div className={'left-label'}>
                <p className={'textp'}>Inserisci Nome:</p>
                <input
                    className={'input-field'}
                    placeholder={"Aggiungi Nome"}
                    required
                    type="text"
                    value={this.state.nome}
                    onChange={this.handleNameChange}
                />

                <div className={'textarea-box'}>
                    <p className={'textp'}>Inserisci Descrizione:</p>
                    <textarea
                        className={'textarea-field'}
                        placeholder={'Aggiungi descrizione'}
                        required
                        rows={5}
                        style={{resize: 'none', width: '300px'}}
                        type="text"
                        value={this.state.descrizione}
                        onChange={this.handleDescriptionChange}
                    />
                </div>

                <p className={'textp'}>Scegli: pubblica o privata:</p>
                <select className={'select-field'} value={this.state.tipoAccesso} onChange={this.handleOptionChange} required>
                    <option value={false}>Pubblica</option>
                    <option value={true}>Privata</option>
                </select>

                <p className={'textp'}>Inserisci numero MAX utenti:</p>
                <input
                    className={'number-field'}
                    required
                    placeholder={'MAX Posti'}
                    type="number"
                    min="1" max="999"
                    style={{width: '100px'}}
                    value={this.maxPosti}
                    onChange={this.handleMAXChange}
                />

            </div>

            <Divider/>

            <div className={'right-label'}>
                <div className={'text-box'}>
                    <p className={'textp'}>Inserisci Codice della Stanza:</p>
                </div>
                    <div className={'pin-box'}>
                        <input
                            className={'pin-field'}
                            required
                            placeholder={'Codice di 6 cifre'}
                            type="text"
                            maxLength={6}
                            style={{width: '150px'}}
                            value={this.state.codiceStanza}
                            onChange={this.handleCodeChange}
                        />
                    </div>
                <br/>
                <button className={'button-create'} type="button" onClick={() =>  this.callFunction() }> Create</button>
            </div>
        </div>
    )};
}
