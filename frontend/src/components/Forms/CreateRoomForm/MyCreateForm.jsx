
import React, {Component} from 'react'
import "./MyCreateForm.css"
import {Divider} from "@chakra-ui/react";

export default class MyCreateForm extends Component{


    //stato di partenza dei parametri
    state = {
        nome: "",
        descrizione: "",
        tipoAccesso: "",
        maxPosti: 0,
        id_scenario: '',            //lo uso per salvare l'id dello scenario e passarlo al backend
        selectedScenario: '',   //per salvarmi quale opzione sceglie l'utente
        array: []       //un array in cui mi salvo i dati provenienti dal backend (lista scenari)
    };

    //funzione per richiamare la funzione per prelevare gli scenari dal backend
    componentDidMount() {
        // Chiamata alla funzione per recuperare l'array di scenari
        this.fetchScenarios();
    }


    requestOption = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };
    fetchScenarios = async () => {
        try {
            const response = await fetch('http://localhost:8080/visualizzaScenari', this.requestOption);

            if (!response.ok) {
                throw new Error('Errore nel recupero degli scenari.');
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
            //esempio di come scorrere l'array per il nome
            /*
                        if (Array.isArray(data.value) && data.value.length > 0) {
                            console.log('Scorrendo array value:');
                            data.value.forEach((item, index) => {
                                const nome = item.nome;
                                console.log(`Elemento ${index + 1}: ${nome}`);
                            });
                        } else {
                            console.error('Nessun elemento trovato nell\'array value');
                        }
             */

                        /* -> altro esempio
                        data.value.map()
                        console.log("nome:", data.value)
                        */

        } catch (error) {
            console.error('Errore durante il recupero degli scenari:', error.message);
        }
    };

    //funzione similar costruttore per settare i valori


//le varie handle richiamate quando passo i valori nelle input form
    handleNameChange = (e) => {
        const inputValue = e.target.value;
      // Trasforma la prima lettera in maiuscolo

            const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
            // Aggiorna lo stato con il nuovo valore
            this.setState({ nome: capitalizedInput, error: null });
    };

    handleDescriptionChange = (e) => {
        const inputValue = e.target.value;

            // Trasforma la prima lettera in maiuscolo
            const capitalizedInput = inputValue.charAt(0).toUpperCase() + inputValue.slice(1);
            // Aggiorna lo stato con il nuovo valore
            this.setState({ descrizione: capitalizedInput, error: null });
    };

    handleOptionChange = (e) => {
        this.setState({tipoAccesso: e.target.value})
    };

    handleMAXChange = (e) => {
        this.setState({maxPosti: e.target.value})
    };


    //funzione alla scelta del parametro nel form
    handleScenarioChange = (e) => {
        //mi salvo la variabile scelta in una const
        const selectedScenarioName = e.target.value;
        console.log('Valore selezionato:', selectedScenarioName);

        //setto la mia variabile selectedscenario con il valore scelto
        this.setState({ selectedScenario: selectedScenarioName });

        //richiamo una funzione per prelevare dal nome scelto, l'id corrispondente
        this.findScenarioByName(selectedScenarioName);
    };



    //funzione trova dal nome
    findScenarioByName = (nome) => {

        const { selectedScenario, array } = this.state;
        console.log("in funziione find->", nome)

        //scorre l'array, e cerca quella entry nell'array che ha quel nome
        const foundScenario = array.find((scenario) => scenario.nome === nome);

        //se l'ha trovata sarà true, => allora mi salva sia la posizione in cui l'ha trovata e sia l'id stesso
        if (foundScenario) {
            const positionInArray = array.indexOf(foundScenario);
            const selectedScenarioId = foundScenario.id;
            console.log(`Nome dello scenario trovato: ${selectedScenario}, ID: ${selectedScenarioId}, Posizione nell'array: ${positionInArray}`);

            // setto lo stato della mia var id_scenario con l'id che è stato scelto
            this.setState({id_scenario: selectedScenarioId});

        } else {
            console.error(`Scenario non trovato per il nome: ${selectedScenario}`);
        }
    };

    /*funzione per inviare i parametri a crea stanza*/
    sendDataToServer = async() =>{
        const {nome, descrizione, tipoAccesso, maxPosti, id_scenario} = this.state;
        const dataToSend = {
            nome,
            descrizione,
            tipoAccesso,
            maxPosti,
            id_scenario
        };

        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
            body: JSON.stringify(dataToSend)
        };

        try{
            console.log("token:" , sessionStorage.getItem("token"))
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


    //chiamata nel momento in cui clicco sul button e mi fa i vari controlli
    handleSubmit = () => {

        //TUTTI QUESTI DIVENTARANNO POP UP (PER GATTO)

        //controllo sul nome
        if (this.state.nome.trim() === '' || this.state.nome.length < 2) {
            this.setState({ error: 'Il campo nome non può essere vuoto o minore di 2 caratteri' });
            return;
        }

        if (!isNaN(this.state.nome.charAt(0))) {
            this.setState({ error: 'Errore durante la richiesta, formato Nome errato' });
            return;
        }

        //controllo sulla descrizione
        if (this.state.descrizione.trim() === '') {
            this.setState({ error: 'Il campo descrizione non può essere vuoto' });
            return;
        }else if(this.state.descrizione.length < 2 || this.state.descrizione.length > 254){
            this.setState({ error: 'Lunghezza descrizione errata' });
            return;
        }

        //controllo max posti
        if(this.state.maxPosti < 1 || this.state.maxPosti > 999){
            this.setState({ error: 'Errore, il massimo di posti deve essere compreso tra 1 e 999' });
            return;
        }

        //controllo tipo accesso
        if (!this.state.tipoAccesso) {
            this.setState({ selectedScenario: '', error: 'Errore,Tipo di Accesso alla stanza non selezionato' });
            return;
        }

        //controllo scenario
        if (!this.state.selectedScenario) {
            this.setState({ selectedScenario: '', error: 'Errore, Selezione Scenario errata' });
            return;
        }

        this.setState({ error: null });

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
                    <select className={'select-field'} value={this.state.tipoAccesso} onChange={this.handleOptionChange}>
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
                        style={{width: '100px'}}
                        value={this.maxPosti}
                        onChange={this.handleMAXChange}
                    />

                    <p className={'textp'}>Seleziona uno scenario</p>
                    <select className={'select-field'} value={this.state.selectedScenario} onChange={this.handleScenarioChange}>
                        <option value="" disabled>Seleziona uno scenario</option>
                        {this.state.array.map((valore) => (
                            <option key={valore.id} value={valore.nome}>
                                {valore.nome}
                            </option>
                        ))}
                    </select>

                </div>

                <Divider/>

                <div className={'right-label'}>
                    <button className={'button-create'} type="button" onClick={() =>  this.handleSubmit() }> Create</button>
                    {this.state.error && <p style={{ color: 'red' }}>{this.state.error}</p>}
                    {/*questo poi diventerà un pop up nel momento in cui clicco su invia*/}
                </div>

            </div>
        )};
}