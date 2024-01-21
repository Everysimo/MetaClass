import React, { useState, useEffect } from 'react';
import {useParams} from "react-router-dom";

const ScenarioPage = () => {
    const [id_scenario, setIdScenario] = useState()
    const [selectedScenario, setSelectedScenario] = useState(null);
    const [array, setArray] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const {id: Id_stanza} = useParams();

    useEffect(() => {
        fetchScenarios();
        // eslint-disable-next-line
    }, []);

    const requestOption = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchScenarios = async () => {
        try {
            const response = await fetch('http://localhost:8080/visualizzaScenari', requestOption);
            if (!response.ok) {
                throw new Error('Errore nel recupero degli scenari.');
            }

            const data = await response.json();
            console.log('data:', data);
            setArray(data.value);

            data.value.forEach((parametro, indice) => {
                const nome = parametro.nome;
                console.log(`Nome ${indice + 1}: ${nome}`);
            });

        } catch (error) {
            console.error('Errore nel recupero degli scenari:', error);
        }
    };

    const handleSelectScenario = (scenario) => {
        setSelectedScenario(scenario);
        setIsModalOpen(true);
    };

    const handleConfirmSelection = () => {
        console.log('Selezione confermata:', selectedScenario);
        setIsModalOpen(false);
        //console.log("l'id settato:", selectedScenario.id)
        setIdScenario(selectedScenario.id)
        console.log("ecco l'id dello scenario", id_scenario)
        console.log("ecco l'id della stanza", Id_stanza)
        sendDataToServer();
    };

    const handleCancelSelection = () => {
        setSelectedScenario(null);
    };

    const sendDataToServer = async () => {

    console.log("selected scenario id", selectedScenario.id)
        console.log("selected stanza id", Id_stanza)

        const dataTosend = {
                id_scenario: parseInt(selectedScenario.id, 10), // converti a numero intero
                idStanza: parseInt(Id_stanza, 10) // converti a numero intero

        }

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify(dataTosend)
        };

        try {
            console.log("la stringa json:", JSON.stringify(dataTosend));
            const response = await fetch(`http://localhost:8080/admin/updateScenario/${encodeURIComponent(dataTosend.idStanza)}/${encodeURIComponent(dataTosend.id_scenario)}`, requestOption);
            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    const handleOpen =()=>{
        setIsModalOpen(true);
    }
    const handleClose=()=>{
        setIsModalOpen(false);
    }
    return (
        <>
        <button
            onClick={handleOpen}
        >
            Modifica scenario
        </button>
            {isModalOpen &&
                <div className={"modal"}>
                    <div className={"modal-content"}>
                        <span
                            className={"close"}
                            onClick={handleClose}
                        >
                            &times;
                        </span>
                        {selectedScenario ? (
                            <div className={"masterDiv"}>
                                <div className={'childDiv'}>
                                    <h2>Conferma la selezione</h2>
                                    <p>Nome: {selectedScenario.nome}</p>
                                    <p>Descrizione: {selectedScenario.descrizione}</p>
                                    <button onClick={handleConfirmSelection}>Conferma</button>
                                    <button onClick={handleCancelSelection}>Annulla</button>
                                </div>
                            </div>
                        ) : (
                            <>
                                <h2>Scegli uno scenario</h2>
                                <div className={"masterDiv"}>
                                    {array.map((scenario) => (
                                        <div key={scenario.id} className="childDiv">
                                            <h3>{scenario.nome}</h3>
                                            <h3>{scenario.id}</h3>
                                            <p>{scenario.descrizione}</p>
                                            <button onClick={() => handleSelectScenario(scenario)}>Scegli</button>
                                        </div>
                                    ))}
                                </div>
                            </>
                        )}
                    </div>
                </div>
            }
        </>
    );
};

export default ScenarioPage;
