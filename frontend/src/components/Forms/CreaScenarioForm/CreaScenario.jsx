import React, { Component } from 'react';
import '../PopUpStyles.css';
import { wait } from "@testing-library/user-event/dist/utils";

export default class CreaScenario extends Component {
    state = {
        nome: "",
        descrizione: "",
        url_immagine: "",
        id_categoria: 0,
        selectedCategoria: '',
        isVisible: true,
        isErrorPopupVisible: false,
        errorMessage: "",
        array: []
    };
    //funzione per richiamare la funzione per prelevare gli scenari dal backend
    componentDidMount() {
        // Chiamata alla funzione per recuperare l'array di scenari
        this.fetchCategoria();
    }
    fetchCategoria = async () => {
        const requestOption1 = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };
        try {
            const response = await fetch('http://localhost:8080/admin/visualizzaCategoria',requestOption1);
            console.log(response.statusMessage);

            if (!response.ok) {
                throw new Error('Errore nel recupero delllle Categorie.');
            }
            //mi vado a salvare la stringa json in una const
            const data = await response.json();
            console.log('data:', data)

            //IMPORTANTE -> QUESTO E' IL METODO PER CONVERTIRE L'ARRAY
            this.setState({array: data.value})
            console.log("array:", this.state.array);

            //questo lo faccio per vedere se riesco a stamparmi un parametro dell'array(uno tra nome, descrizione, id, media...)
            this.state.array.forEach((parametro, indice) => {
                const descr = parametro.descrizione;
                console.log(`Descrizione ${indice + 1}: ${descr}`);
            });

        } catch (error) {
            console.error('Errore durante il recupero delle Categorie:', error.message);
        }
    };




    handleNameChange = (e) => {
        const inputValue = e.target.value;

        //prima lettera grande
        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        this.setState({ nome: capitalizedInput, error: null });

    };
    handleCategoriaChange = (e) => {
        // mi salvo la variabile scelta in una const
        const selectedCategoriaName = e.target.value;
        console.log('Valore selezionato:', selectedCategoriaName);

        // setto la mia variabile selectedCategoria con il valore scelto
        this.setState({ selectedCategoria: selectedCategoriaName });

        // richiamo una funzione per prelevare dal nome scelto, l'id corrispondente
        this.findCategoriaByName(selectedCategoriaName);
    };

    findCategoriaByName = (nome) => {

        const { selectedCategoria, array } = this.state;
        console.log("in funziione find->", nome)

        //scorre l'array, e cerca quella entry nell'array che ha quel nome
        const foundCategoria = array.find((scenario) => scenario.nome === nome);

        //se l'ha trovata sarà true, => allora mi salva sia la posizione in cui l'ha trovata e sia l'id stesso
        if (foundCategoria) {
            const positionInArray = array.indexOf(foundCategoria);
            const selectedCategoriaId = foundCategoria.id;
            console.log(`Nome dello scenario trovato: ${selectedCategoria}, ID: ${selectedCategoriaId}, Posizione nell'array: ${positionInArray}`);

            // setto lo stato della mia var id_scenario con l'id che è stato scelto
            this.setState({id_categoria: selectedCategoriaId});

        } else {
            console.error(`Categoria non trovata per il nome: ${selectedCategoria}`);
        }
    };



    handleDescChange = (e) => {
        const inputValue = e.target.value;

        const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
        this.setState({ descrizione: capitalizedInput, error: null });
    };
    handleImmageChange = (e) => {
        this.setState({ url_immagine: e.target.value });
    };

    handleErrorPopupClose = () => {
        this.setState({
            isErrorPopupVisible: false,
            errorMessage: "",
        });
    };

    sendDataToServer = async () => {
        const { nome, descrizione, url_immagine, id_categoria } = this.state;

        // Validazione: Assicurati che idCategoria sia >= 0

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
            console.log("la stringa json:", JSON.stringify(dataToSend));
            const response = await fetch('http://localhost:8080/admin/updateScenario', requestOption);
            const responseData = await response.json();
            console.log("Risposta dal server:", responseData);
        } catch (error) {
            console.error('ERRORE:', error);
        }
    };

    callFunction = () => {

        //CONTROLLI DA TRASFORMARE IN POP UP
        if (this.state.nome.trim() === '' || this.state.nome.length < 2) {
            this.setState({ error: 'Il campo nome non può essere vuoto o minore di 2 caratteri' });
            return;
        }

        if (this.state.descrizione.trim() === '') {
            this.setState({error: 'Il campo descrizione non può essere vuoto'});
            return;
        }else if(this.state.descrizione.length < 2 || this.state.descrizione.length > 254) {
            this.setState({error: 'Lunghezza descrizione errata'});
            return;
        }else if(!isNaN(this.state.descrizione.charAt(0))) {
            this.setState({ error: 'Errore durante la richiesta, formato Nome errato' });
            return;
        }
        //controllo scenario
        if (!this.state.selectedCategoria) {
            this.setState({ selectedCategoria: '', error: 'Errore, Selezione Categoria errata' });
            return;
        }

        this.setState({ error: null });
        this.sendDataToServer();
        console.log("dati del form", this.state);
        wait(100);
        this.handleClear();
    };

    handleClear = () => {
        this.setState({
            nome: '',
            descrizione: '',
            url_immagine: '',
            id_categoria: 0,
        });
    };
    handleClose = () => {
        // Nascondi la card impostando isVisible su false
        this.setState({ isVisible: false });

        // Chiama la funzione di chiusura ricevuta come prop
        if (this.props.onClose) {
            this.props.onClose();
        }
    };
    renderErrorPopup = () => {
        return (
            <div className={`error-popup ${this.state.isErrorPopupVisible ? '' : 'hidden'}`}>
                {this.state.errorMessage}
                <button onClick={this.handleErrorPopupClose}>Chiudi</button>
            </div>
        );
    };

    render() {
        return (
            <div>
                {this.renderErrorPopup()}
                <div className={`card ${this.state.isVisible ? '' : 'hidden'}`}>
                    <button className="close-button" onClick={this.handleClose}>
                        X
                    </button>
                    <div className="card-content">
                        <label>
                            Nome:
                            <input
                                type="text"
                                name="nome"
                                value={this.state.nome}
                                onChange={this.handleNameChange}
                            />
                        </label>
                        <label>
                            Descrizione:
                            <input
                                type="text"
                                name="descrizione"
                                value={this.state.descrizione}
                                onChange={this.handleDescChange}
                            />
                        </label>
                        <label>
                            Immagine:
                            <input
                                type="text"
                                name="immagine"
                                value={this.state.url_immagine}
                                onChange={this.handleImmageChange}
                            />
                        </label>
                        <label>
                            <p className={'textp'}>Seleziona una categoria</p>
                            <select className={'select-field'} value={this.state.selectedCategoria}
                                    onChange={this.handleCategoriaChange}>
                                <option value="" disabled>Seleziona una categoria</option>
                                {this.state.array.map((valore) => (
                                    <option key={valore.id} value={valore.nome}>
                                        {valore.nome}
                                    </option>
                                ))}
                            </select>
                        </label>
                        <div className="button-container">
                            <button onClick={this.handleClear}>Cancella</button>
                            <button onClick={() => this.callFunction()}>Invia</button>
                        </div>
                        {this.state.error && <p style={{color: 'red'}}>{this.state.error}</p>}
                    </div>
                </div>
            </div>
        );
    };
}
