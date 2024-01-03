import React, { useState } from 'react';

const AccediStanza = () => {
    const [codiceStanza, setCodiceStanza] = useState('');
    const [isVisible, setIsVisible] = useState(true);


    const handleInputChange = (event) => {
        setCodiceStanza(event.target.value);
    };

    const handleClear = () => {
        setCodiceStanza('');
    };

    const handleSend = () => {
        console.log('Dati inseriti:', codiceStanza);

        // Resetta il campo di inserimento
        setCodiceStanza('');
        if (/^\d{6}$/.test(codiceStanza)) {
            // Esegui azione con i dati inseriti (ad esempio, invia a un server)
            console.log('Dati inseriti:', codiceStanza);

            // Resetta il campo di inserimento
            setCodiceStanza('');
        } else {
            // Gestisci il caso in cui il formato non è corretto (mostra un messaggio di errore, ad esempio)
            alert('Il formato inserito non è corretto. Assicurati che siano 6 cifre numeriche.');
        }
        sendDataToServer();
    };

    const sendDataToServer = async() =>{
        const {codiceStanza} = this.state;
        const dataToSend = {
            codiceStanza: codiceStanza,
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
                    insert room code:
                    <input
                        type="text"
                        value={this.state.codiceStanza}
                        onChange={handleInputChange}
                        maxLength={6}
                        minLength={6}
                        pattern="\d*"
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

export default AccediStanza;
