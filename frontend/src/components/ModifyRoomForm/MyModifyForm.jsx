import React, { useState } from 'react';
import { Divider } from "@chakra-ui/react";
import "./MyModifyForm.css";

const MyModifyForm = () => {
    const Id = localStorage.getItem('UserMetaID')
    const [state, setState] = useState({
        nome: "",
        descrizione: "",
        tipoAccesso: '',
        maxPosti: '',
    });

    const responseForm = (response) => {
        setState({
            nome: response.nome,
            descrizione: response.descrizione,
            tipoAccesso: response.tipoAccesso,
            maxPosti: response.maxPosti,
        });
    };

    const handleNameChange = (e) => {
        setState({ ...state, nome: e.target.value });
    };

    const handleDescriptionChange = (e) => {
        setState({ ...state, descrizione: e.target.value });
    };

    const handleOptionChange = (e) => {
        setState({ ...state, tipoAccesso: e.target.value });
    };

    const handleMAXChange = (e) => {
        setState({ ...state, maxPosti: e.target.value });
    };

    /*funzioni per eliminare la stanza*/
    const deleteButton = () => {
        console.log("sono nel button delete");
        handleDeleteRoom();
    };

    const handleDeleteRoom = async () => {
        const requestOption = {
            method: 'POST',
            headers: {'Content-Type': 'application/json' }
        };

        try {
            const response = await fetch('http://localhost:8080/eliminaStanza', requestOption);
            const responseData = await response.json();
            console.log("Risposta del server:", responseData);
        } catch (error) {
            console.error('Errore:', error);
        }
    };

    const sendDataModifyRoom = async () => {

        const {nome, descrizione, tipoAccesso, maxPosti} = state;
        const dataToSend = {};

        // Aggiungi solo i campi non vuoti all'oggetto dataToSend
        if (nome) {
            dataToSend.nome = nome;
        }
        if (descrizione) {
            dataToSend.descrizione = descrizione;
        }
        if (tipoAccesso) {
            dataToSend.tipoAccesso = tipoAccesso;
        }
        if (maxPosti) {
            dataToSend.maxPosti = maxPosti;
        }

        // Verifica se l'oggetto dataToSend è vuoto
        if (Object.keys(dataToSend).length === 0) {
            console.error('Nessun dato da inviare.');
            return;
        }

        const requestOption = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(dataToSend)
        };

        try {
            console.log("id: 725841336137765 ->", Id);
            console.log("la stringa json:", JSON.stringify(dataToSend));

            const response = await fetch(`http://localhost:8080/modifyRoomData/${1}`, requestOption);

            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    }

    return (
        <>
            <div className={'primary'}>
                <div className={'left-label'}>
                    <h4>Modifica i dati relativi ad una stanza:</h4>
                    <p className={'textp'}>Inserisci Nuovo Nome:</p>
                    <input
                        className={'input-field'}
                        placeholder={"Aggiungi Nuovo Nome"}
                        type="text"
                        value={state.nome}
                        onChange={handleNameChange}
                    />

                    <div className={'textarea-box'}>
                        <p className={'textp'}>Inserisci Nuova Descrizione:</p>
                        <textarea
                            className={'textarea-field'}
                            placeholder={'Aggiungi Descrizione'}
                            rows={5}
                            style={{ resize: 'none', width: '300px' }}
                            type="text"
                            value={state.descrizione}
                            onChange={handleDescriptionChange}
                        />
                    </div>

                    <p className={'textp'}>Scegli: pubblica o privata:</p>
                    <select className={'select-field'} value={state.tipoAccesso} onChange={handleOptionChange}>
                        <option value=""> -Scegli un'opzione-</option>
                        <option value="false">Pubblica</option>
                        <option value="true">Privata</option>
                    </select>

                    <p className={'textp'}>Inserisci numero MAX utenti:</p>
                    <input
                        className={'number-field'}
                        placeholder={'MAX Posti'}
                        type="number"
                        min="1" max="999"
                        style={{ width: '100px' }}
                        value={state.maxPosti}
                        onChange={handleMAXChange}
                    />
                </div>
                <Divider />
                <br />

                <button className={'button-delete'} type="button" onClick={deleteButton}> Elimina </button>
                <button className={'button-modify'} type="button" onClick={() => sendDataModifyRoom()}> Modifica </button>
            </div>
        </>
    );
};
export default MyModifyForm;
