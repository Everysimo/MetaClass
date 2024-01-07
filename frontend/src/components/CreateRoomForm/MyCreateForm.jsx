
import React, {Component} from 'react'
import "./MyCreateForm.css"
import {Divider} from "@chakra-ui/react";

export default class MyCreateForm extends Component{


    //stato di partenza dei parametri
    state = {
        nome: "",
        codiceStanza: "",
        descrizione: "",
        tipoAccesso: false,
        maxPosti: 0,
        id_scenario: '',
        selectedScenario: '',
        array: []
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
            const data = await response.json();
            console.log('data:', data)

            this.setState({array: data.value})

            console.log("array:", this.state.array);

            this.state.array.forEach((parametro, indice) => {
                const descr = parametro.descrizione;
                console.log(`Descrizione ${indice + 1}: ${descr}`);
            });
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

                        /*
                        data.value.map()
                        console.log("nome:", data.value)
                        */

        } catch (error) {
            console.error('Errore durante il recupero degli scenari:', error.message);
        }
    };

    //funzione similar costruttore per settare i valori
    responseForm = response => {
        this.setState({
            nome: response.nome,
            codiceStanza: response.codiceStanza,
            descrizione: response.descrizione,
            tipoAccesso: response.tipoAccesso,
            maxPosti: response.maxPosti,
            selectedScenario: response.selectedScenario
        });
    };

//le varie handle richiamate quando passo i valori nelle input form
    handleNameChange = (e) => {
        this.setState({nome: e.target.value});
    };

    handleCodeChange = (e) => {
        this.setState({codiceStanza: e.target.value})
    };

    handleDescriptionChange = (e) => {
        this.setState({descrizione: e.target.value})
    };

    handleOptionChange = (e) => {
        this.setState({tipoAccesso: e.target.value})
    };

    handleMAXChange = (e) => {
        this.setState({maxPosti: e.target.value})
    };


    handleScenarioChange = (e) => {
        const selectedScenarioName = e.target.value;
        console.log('Valore selezionato:', selectedScenarioName);

        this.setState({ selectedScenario: selectedScenarioName });

        this.findScenarioByName(selectedScenarioName);
    };



    findScenarioByName = (nome) => {

        const { selectedScenario, array } = this.state;
        console.log("in funziione find->", nome)

        const foundScenario = array.find((scenario) => scenario.nome === nome);

        if (foundScenario) {
            const positionInArray = array.indexOf(foundScenario);
            const selectedScenarioId = foundScenario.id;

            console.log(`Nome dello scenario trovato: ${selectedScenario}, ID: ${selectedScenarioId}, Posizione nell'array: ${positionInArray}`);

            this.setState({id_scenario: selectedScenarioId});

            // Salva la posizione e l'id nello stato o fai qualsiasi altra cosa tu voglia
            // this.setState({ positionInArray, selectedScenarioId });
        } else {
            console.error(`Scenario non trovato per il nome: ${selectedScenario}`);
        }
    };



    /*funzione per inviare i parametri a crea stanza*/
    sendDataToServer = async() =>{
        const {nome, codiceStanza, descrizione, tipoAccesso, maxPosti, id_scenario} = this.state;
        const dataToSend = {
            nome,
            codiceStanza,
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
                        required
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
                    <select className={'select-field'} value={this.state.tipoAccesso} onChange={this.handleOptionChange}
                            required>
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


                    {/*

                <select className={'select-field'} value={this.state.scenarios} onChange={this.handleScenarioChange}>
                    <option value="" disabled>Seleziona uno scenario</option>
                    {Object.entries(this.state.scenarios).map(([key, value]) => (

                        <option key={key} value={value}>
                            {this.state.scenarios}

                        </option>

                    ))}

                </select>
*/}
                </div>

                <Divider/>

                <div className={'right-label'}>
                    <div className={'text-box'}>
                        <p className={'textp'}>Inserisci Codice della Stanza:</p>
                    </div>
                    <div className={'pin-box'}>
                        <input
                            className={'pin-field'}
                            required
                            placeholder={'Codice di 6 cifre'}
                            type="text"
                            maxLength={6}
                            style={{width: '150px'}}
                            value={this.state.codiceStanza}
                            onChange={this.handleCodeChange}
                        />
                    </div>
                    <br/>
                    <button className={'button-create'} type="button" onClick={() =>  this.callFunction() }> Create</button>
                </div>
            </div>
        )};
}