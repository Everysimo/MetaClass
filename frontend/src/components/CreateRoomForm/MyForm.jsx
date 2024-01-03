
import React, {Component} from 'react'
import "./MyForm.css"
import {Divider} from "@chakra-ui/react";

export default class MyForm extends Component{


    //stato di partenza dei parametri
    state = {
        name: "",
        descript: "",
        option: "",
        num: "",
        code: ""
    };

    //funzione similar costruttore per settare i valori
    responseForm = response => {
        this.setState({
            name: response.name,
            descript: response.descript,
            option: response.option,
            num: response.num,
            code: response.code
        });
    };

//le varie handle richiamate quando passo i valori nelle input form
    handleNameChange = (e) => {
        this.setState({name: e.target.value});
    };

    handleDescriptionChange = (e) => {
        this.setState({descript: e.target.value})
    };

    handleOptionChange = (e) => {
        this.setState({option: e.target.value})
    };

    handleMAXChange = (e) => {
        this.setState({num: e.target.value})
    };


    handleCodeChange = (e) => {
        this.setState({code: e.target.value})
    };


    /*funzione per inviare i parametri a crea stanza*/

    sendDataToServer = async() =>{
        const {name, descript, option, num, code} = this.state;
        const dataToSend = {
            name,
            descript,
            option,
            num,
            code
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
                    type="text"
                    value={this.state.name}
                    onChange={this.handleNameChange}
                />

                <div className={'textarea-box'}>
                    <p className={'textp'}>Inserisci Descrizione:</p>
                    <textarea
                        className={'textarea-field'}
                        placeholder={'Aggiungi descrizione'}
                        rows={5}
                        style={{resize: 'none', width: '300px'}}
                        type="text"
                        value={this.state.descript}
                        onChange={this.handleDescriptionChange}
                    />
                </div>

                <p className={'textp'}>Scegli: pubblica o privata:</p>
                <select className={'select-field'} value={this.state.option} onChange={this.handleOptionChange}>
                    <option value=""> -Scegli un'opzione-</option>
                    <option value="public">Pubblica</option>
                    <option value="private">Privata</option>
                </select>

                <p className={'textp'}>Inserisci numero MAX utenti:</p>
                <input
                    className={'number-field'}
                    placeholder={'MAX Posti'}
                    type="number"
                    min="1" max="999"
                    style={{width: '100px'}}
                    value={this.num}
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
                            placeholder={'Codice di 6 cifre'}
                            type="text"
                            maxLength={6}
                            style={{width: '150px'}}
                            value={this.state.code}
                            onChange={this.handleCodeChange}
                        />
                    </div>
                <br/>
                <button className={'button-create'} type="button" onClick={() =>  this.callFunction() }> Create</button>
            </div>
        </div>
    )};
}
