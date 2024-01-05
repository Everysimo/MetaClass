// ScenarioPage.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import './SelectScenario.css';
Modal.setAppElement('#root');

const ScenarioPage = () => {
    //const [scenarios, setScenarios] = useState([]);
    const [selectedScenario, setSelectedScenario] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const scenarios = [
        { id: 1, name: 'Mare', description: 'bellissimo policastro' },
        { id: 2, name: 'Montagna', description: 'bruttissimo sanza' },
        // Aggiungi altri dati di richieste di accesso secondo necessità
    ];
/*
    useEffect(() => {
        const fetchScenarios = async () => {
            try {
                const response = await axios.get('URL_DEL_BACKEND'); // Sostituisci 'URL_DEL_BACKEND' con l'effettivo URL del tuo backend
                setScenarios(response.data);
            } catch (error) {
                console.error('Errore nel recupero degli scenari:', error);
            }
        };

        fetchScenarios();
    }, []);
*/
    const handleSelectScenario = (scenario) => {
        setSelectedScenario(scenario);
        setIsModalOpen(true);
    };

    const handleConfirmSelection = () => {
        // Qui puoi implementare la logica per gestire la conferma della selezione
        console.log('Selezione confermata:', selectedScenario);
        setIsModalOpen(false);
    };

    const handleCancelSelection = () => {
        setSelectedScenario(null);
        setIsModalOpen(false);
    };

    return (
        <div>
            <h2>Scegli uno scenario</h2>
            {scenarios.map((scenario) => (
                <div key={scenario.id} className="card">
                    <h3>{scenario.name}</h3>
                    <p>{scenario.description}</p>
                    <button onClick={() => handleSelectScenario(scenario)}>Scegli</button>
                </div>
            ))}

            <Modal
                isOpen={isModalOpen}
                onRequestClose={() => setIsModalOpen(false)}
                isCentered
                size={'xs'}
                contentLabel="Conferma Selezione"
                className={'Modal'}
            >
                {selectedScenario && (
                    <div className={'modal-box'}>
                        <h2>Conferma la selezione</h2>
                        <p>Nome: {selectedScenario.name}</p>
                        <p>Descrizione: {selectedScenario.description}</p>
                        <button onClick={handleConfirmSelection}>Conferma</button>
                        <button onClick={handleCancelSelection}>Annulla</button>
                    </div>
                )}
            </Modal>
        </div>
    );
};

export default ScenarioPage;
