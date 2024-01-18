import React, { useState, useEffect } from 'react';
import '../PopUpStyles.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCirclePlus} from "@fortawesome/free-solid-svg-icons";
const MyCreateForm = () => {
    const [showCreateFormModal, setShowCreateFormModal] = useState(false);
    const handleShowCreateForm = () => {
        setShowCreateFormModal(true);
    };

    const handleCloseCreateFormModal = () => {
        setShowCreateFormModal(false);
    };
    const [state, setState] = useState({
        nome: "",
        descrizione: "",
        tipoAccesso: "",
        maxPosti: 0,
        id_scenario: '',
        selectedScenario: '',
        array: []
    });

    useEffect(() => {
        fetchScenarios();
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
            setState(prevState => ({ ...prevState, array: data.value }));

            state.array.forEach((parametro, indice) => {
                const descr = parametro.descrizione;
                console.log(`Descrizione ${indice + 1}: ${descr}`);
            });
        } catch (error) {
            console.error('Errore durante il recupero degli scenari:', error.message);
        }
    };

    const handleNameChange = (e) => {
        const inputValue = e.target.value;
        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        setState(prevState => ({ ...prevState, nome: capitalizedInput, error: null }));
    };

    const handleDescriptionChange = (e) => {
        const inputValue = e.target.value;
        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        setState(prevState => ({ ...prevState, descrizione: capitalizedInput, error: null }));
    };

    const handleOptionChange = (e) => {
        setState(prevState => ({ ...prevState, tipoAccesso: e.target.value }));
    };

    const handleMAXChange = (e) => {
        setState(prevState => ({ ...prevState, maxPosti: e.target.value }));
    };

    const handleScenarioChange = (e) => {
        const selectedScenarioName = e.target.value;
        setState(prevState => ({ ...prevState, selectedScenario: selectedScenarioName }));
        findScenarioByName(selectedScenarioName);
    };

    const findScenarioByName = (nome) => {
        const { selectedScenario, array } = state;
        const foundScenario = array.find((scenario) => scenario.nome === nome);

        if (foundScenario) {
            const positionInArray = array.indexOf(foundScenario);
            const selectedScenarioId = foundScenario.id;
            console.log(`Nome dello scenario trovato: ${selectedScenario}, ID: ${selectedScenarioId}, Posizione nell'array: ${positionInArray}`);
            setState(prevState => ({ ...prevState, id_scenario: selectedScenarioId }));
        } else {
            console.error(`Scenario non trovato per il nome: ${selectedScenario}`);
        }
    };

    const sendDataToServer = async () => {
        const { nome, descrizione, tipoAccesso, maxPosti, id_scenario } = state;
        const dataToSend = { nome, descrizione, tipoAccesso, maxPosti, id_scenario };

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify(dataToSend)
        };

        try {
            console.log("token:", sessionStorage.getItem("token"))
            console.log("la stringa json:", JSON.stringify(dataToSend));
            const response = await fetch('http://localhost:8080/creastanza', requestOption);
            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    const handleSubmit = () => {
        if (state.nome.trim() === '' || state.nome.length < 2) {
            setState(prevState => ({
                ...prevState,
                error: 'Il campo nome non può essere vuoto o minore di 2 caratteri'
            }));
            return;
        }

        if (!isNaN(state.nome.charAt(0))) {
            setState(prevState => ({
                ...prevState,
                error: 'Errore durante la richiesta, formato Nome errato'
            }));
            return;
        }

        if (state.descrizione.trim() === '') {
            setState(prevState => ({
                ...prevState,
                error: 'Il campo descrizione non può essere vuoto'
            }));
            return;
        } else if (state.descrizione.length < 2 || state.descrizione.length > 254) {
            setState(prevState => ({
                ...prevState,
                error: 'Lunghezza descrizione errata'
            }));
            return;
        }

        if (state.maxPosti < 1 || state.maxPosti > 999) {
            setState(prevState => ({
                ...prevState,
                error: 'Errore, il massimo di posti deve essere compreso tra 1 e 999'
            }));
            return;
        }

        if (!state.tipoAccesso) {
            setState(prevState => ({
                ...prevState,
                selectedScenario: '',
                error: 'Errore,Tipo di Accesso alla stanza non selezionato'
            }));
            return;
        }

        if (!state.selectedScenario) {
            setState(prevState => ({
                ...prevState,
                selectedScenario: '',
                error: 'Errore, Selezione Scenario errata'
            }));
            return;
        }

        setState(prevState => ({ ...prevState, error: null }));
        sendDataToServer();
        console.log("dati del form", state)
    };

    return (
        <>
            <div
                className={"transWhiteBg"}
                onClick={handleShowCreateForm}
            >
                <FontAwesomeIcon icon={faCirclePlus} size="2xl" style={{color: "#c70049",}}/>
                <h2>Creazione stanza</h2>
                <p
                    style={{fontSize: "14px", textAlign: "center",}}
                >
                    Crea una stanza tutta tua e diventa Organizzatore
                </p>
            </div>
            {showCreateFormModal && (
                <div className="modal">
                    <div className="modal-content">
                        {/* Add a close button inside the modal */}
                        <span
                            className="close"
                            onClick={handleCloseCreateFormModal}
                        >
                            &times;
                        </span>
                        <div className={"masterDiv"}>
                            <div className={"childDiv"}>
                                <p className={'textp'}>Inserisci Nome:</p>
                                <input
                                    className={'input-field'}
                                    placeholder={"Aggiungi Nome"}
                                    type="text"
                                    value={state.nome}
                                    onChange={handleNameChange}
                                />
                                <div className={"textarea-box"}>
                                    <p className={'textp'}>Inserisci Descrizione:</p>
                                    <textarea
                                        className={'textarea-field'}
                                        placeholder={'Aggiungi descrizione'}
                                        required
                                        rows={5}
                                        type="text"
                                        value={state.descrizione}
                                        onChange={handleDescriptionChange}
                                    />
                                </div>
                            </div>
                            <div className={"childDiv"}>
                                <p className={'textp'}>Scegli: pubblica o privata:</p>
                                <select className={'select-field'} value={state.tipoAccesso}
                                        onChange={handleOptionChange}>
                                    <option value="" disabled>Seleziona Tipo Accesso</option>
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
                                    value={state.maxPosti}
                                    onChange={handleMAXChange}
                                />
                                <p className={'textp'}>Seleziona uno scenario</p>
                                <select className={'select-field'} value={state.selectedScenario}
                                        onChange={handleScenarioChange}>
                                    <option value="" disabled>Seleziona uno scenario</option>
                                    {state.array.map((valore) => (
                                        <option key={valore.id} value={valore.nome}>
                                            {valore.nome}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>
                        <button
                            className={'button-create'}
                            type="button"
                            onClick={handleSubmit}
                        >
                            Create
                        </button>
                        {state.error && <p style={{color: 'red'}}>{state.error}</p>}
                    </div>
                </div>
            )}
        </>
    );
};

export default MyCreateForm;
