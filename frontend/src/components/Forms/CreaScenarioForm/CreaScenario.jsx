import React, { useState, useEffect } from 'react';
import '../PopUpStyles.css';
import { wait } from "@testing-library/user-event/dist/utils";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheck } from "@fortawesome/free-solid-svg-icons";

const CreaScenario = (props) => {
    const [nome, setNome] = useState("");
    const [descrizione, setDescrizione] = useState("");
    const [url_immagine, setUrlImmagine] = useState("");
    const [id_categoria, setIdCategoria] = useState(0);
    const [selectedCategoria, setSelectedCategoria] = useState('');
    const [errorMessage, setErrorMessage] = useState("");
    const [array, setArray] = useState([]);
    const [showCreateFormModal, setShowCreateFormModal] = useState(true);
    const [showSuccessPopUp, setShowSuccessPopUp] = useState(false);
    const [selectedFile, setSelectedFile] = useState("")


    useEffect(() => {
        fetchCategoria();
    }, []);

    const handleShowCreateForm = () => {
        setShowCreateFormModal(true);
    };

    const handleCloseCreateFormModal = () => {
        setShowCreateFormModal(false);
    };

    const requestOption1 = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };
    const fetchCategoria = async () => {
        try {
            const response = await fetch('http://localhost:8080/admin/visualizzaCategoria', requestOption1);
            console.log(response.statusMessage);

            if (!response.ok) {
                throw new Error('Errore nel recupero delle Categorie.');
            }


            const data = await response.json();
            console.log('data con categoria:', data)

            setArray(data.value);
            console.log('array con categoria:', data.value);

        } catch (error) {
            console.error('Errore durante il recupero delle Categorie:', error.message);
        }
    };

    const handleNameChange = (e) => {
        const inputValue = e.target.value;
        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        setNome(capitalizedInput);
    };

    const handleCategoriaChange = (e) => {
        const selectedCategoriaName = e.target.value;
        setSelectedCategoria(selectedCategoriaName);
        findCategoriaByName(selectedCategoriaName);
    };

    const findCategoriaByName = (nome) => {
        const foundCategoria = array.find((scenario) => scenario.nome === nome);

        if (foundCategoria) {
            const selectedCategoriaId = foundCategoria.id;
            setIdCategoria(selectedCategoriaId);
        } else {
            console.error(`Categoria non trovata per il nome: ${selectedCategoria}`);
        }
    };

    const handleDescChange = (e) => {
        const inputValue = e.target.value;
        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        setDescrizione(capitalizedInput);
    };

    const handleImageChange = (e) => {
        setSelectedFile(e.target.files[0])

        if (selectedFile) {
            // Qui puoi gestire il file selezionato, ad esempio, leggere il nome del file o eseguire altre operazioni necessarie
            console.log("Nome del file:", selectedFile.name);
        }
    };

    const sendDataToServer = async () => {
        const url_immagine = '/'+selectedFile.name;

        const dataToSend = {
            nome,
            descrizione,
            url_immagine,
            id_categoria,
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
            console.log("datatosend", dataToSend)
            const response = await fetch('http://localhost:8080/admin/updateScenario', requestOption);
            const responseData = await response.json();

            if (response.ok) {
                console.log('Valutazione inviata con successo!');

                handleCloseCreateFormModal();
                setShowSuccessPopUp(true);

            } else {
                console.error('Errore durante l\'invio della valutazione al backend');
            }
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    const callFunction = () => {
        if (nome.trim() === '' || nome.length < 2) {
            setErrorMessage('Il campo nome non può essere vuoto o minore di 2 caratteri');
            return;
        }

        if (descrizione.trim() === '') {
            setErrorMessage('Il campo descrizione non può essere vuoto');
            return;
        } else if (descrizione.length < 2 || descrizione.length > 254) {
            setErrorMessage('Lunghezza descrizione errata');
            return;
        } else if (!isNaN(descrizione.charAt(0))) {
            setErrorMessage('Errore durante la richiesta, formato Nome errato');
            return;
        }

        if (!selectedFile) {
            setErrorMessage('Errore: immagine non selezionata');
            return;
        }

        if (!selectedCategoria) {
            setSelectedCategoria('');
            setErrorMessage('Errore, Selezione Categoria errata');
            return;
        }

        setErrorMessage(null);
        sendDataToServer();
        console.log("dati del form", { nome, descrizione, url_immagine, id_categoria });
        wait(100);
        handleClear();
    };

    const handleClear = () => {
        setNome('');
        setDescrizione('');
        setUrlImmagine('');
        setIdCategoria(0);
    };

    const handleClose = () => {
        if (props.onClose) {
            props.onClose();
        }
    };

    const handleCloseSuccessPopUp = () => {
        setShowSuccessPopUp(false);
    };

    return (
        <>
            {showCreateFormModal && (
            <div className={"modal"}>
                <div className={`modal-content`}>
                    <span className="close" onClick={handleClose}>&times;</span>
                        <div className="card-content">
                            <label>
                                Nome:
                                <input
                                    type="text"
                                    name="nome"
                                    placeholder='Inserisci Nome'
                                    value={nome}
                                    onChange={handleNameChange}
                                    style={{marginBlock: "10px"}}
                                />
                            </label>
                            <label>
                                Descrizione:
                                <input
                                    type="text"
                                    name="descrizione"
                                    placeholder='Inserisci Descrizione'
                                    value={descrizione}
                                    onChange={handleDescChange}
                                    style={{marginBlock: "10px"}}
                                />
                            </label>
                            <label>
                                Immagine:
                                <input
                                    type="file"
                                    name="immagine"
                                    accept="image/*"
                                    onChange={handleImageChange}
                                />
                            </label>
                            <label>
                                <p className={'textp'}>Seleziona una categoria</p>
                                <select className={'select-field'} value={selectedCategoria}
                                        onChange={handleCategoriaChange}>
                                    <option value="" disabled>Seleziona una categoria</option>
                                    {Array.isArray(array) && array.map((valore) => (
                                        <option key={valore.id} value={valore.nome}>
                                            {valore.nome}
                                        </option>
                                    ))}
                                </select>
                            </label>
                            <button onClick={handleClear}>Cancella</button>
                            <button onClick={callFunction}>Invia</button>
                            {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                        </div>
                </div>
            </div>
                    )}
            {showSuccessPopUp && (
                <div className={"modal"}>
                    <div className={"modal-content"}>
                        <span className={"close"} onClick={handleCloseSuccessPopUp}>&times;</span>
                        <p>Scenario creato con successo <FontAwesomeIcon icon={faCheck} size="2xl" style={{ color: "#63E6BE" }} /></p>
                    </div>
                </div>
            )}
    </>
    );
};

export default CreaScenario;
