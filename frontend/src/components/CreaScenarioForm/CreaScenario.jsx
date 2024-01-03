import React, { useState } from 'react';
import './creaScenario.css';

sendDataToServer = async() =>{
    const {nome, descrizione} = this.state;
    const dataToSend = {
        nome,
        descrizione
    };

    const requestOption = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dataToSend)
    };

    try{
        console.log("la stringa json:", JSON.stringify(dataToSend));
        //col fetch faccio la richiesta, al URL descritto
        const response = await fetch('http://localhost:8080/', requestOption);//da comprendre
        //con la await attento la risposta dal fetch
        const responseData = await response.json();
        console.log("Risposta dal server:", responseData);
    }catch(error){
        console.error('ERRORE:', error);
    }


}
const CreaScenario = () => {
    const [nome, setNome] = useState('');
    const [descrizione, setDescrizione] = useState('');
    const [isVisible, setIsVisible] = useState(true);

    const handleNomeChange = (event) => {
        const value = event.target.value;
        if (value.length <= 255 || value === '') {
            setNome(value);
        }
    };

    const handleDescrizioneChange = (event) => {
        const value = event.target.value;

        // Verifica la lunghezza massima di 255 caratteri
        if (value.length <= 255 || value === '') {
            setDescrizione(value);
        }
    };

    const handleClear = () => {
        setNome('');
        setDescrizione('');
    };

    const handleSend = () => {
        this.sendDataToServer();
        console.log("dati del form", this.state)

        // Resetta i campi di inserimento
        setNome('');
        setDescrizione('');
    };

    const handleClose = () => {
        // Nascondi la card impostando isVisible su false
        setIsVisible(false);
    };

    return (
        <div className={`card ${isVisible ? '' : 'hidden'}`}>
            <button className="close-button" onClick={handleClose}>
                X
            </button>
            <div className="card-content">
                <label>
                    Nome:
                    <input
                        type="text"
                        value={nome}
                        onChange={handleNomeChange}
                        maxLength={255}
                    />
                    Descrizione:
                    <input
                        type="text"
                        value={descrizione}
                        onChange={handleDescrizioneChange}
                        maxLength={255}
                    />
                </label>
                <div className="button-container">
                    <button onClick={handleClear}>Cancella</button>
                    <button onClick={handleSend}>Invia</button>
                </div>
            </div>
        </div>
    );
};

export default CreaScenario;
