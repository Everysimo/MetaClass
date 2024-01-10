import React, { Component } from 'react';
import {Link} from "react-router-dom";

class RoomList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            array: [],
        };
    }


    componentDidMount() {
        this.fetchElencoStanze();

    }

    fetchElencoStanze = async () => {

        const requestOption = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: 'Bearer ' + sessionStorage.getItem('token'),
            },
        };

        try {
            const response = await fetch('http://localhost:8080/visualizzaStanze', requestOption);

            if (!response.ok) {
                throw new Error('Errore nel recupero degli scenari.');
            }

            const data = await response.json();
            console.log("eccoli i dati delle stanze:", data);

            this.setState({ array: data.value });
            console.log("nell'array:", this.state.array);

            this.state.array.forEach((param, indice) => {
                const nome = param.nome;
                console.log(`nome ${indice + 1}: ${nome}`);
            });
        } catch (error) {
            console.error('Errore durante il recupero degli scenari:', error.message);
        }
    };

    render() {
        return (
            <>
                <div>
                    <h1>Elenco Stanze</h1>
                    {/*
                    <button>
                        <Link to="/SingleRoom" style={{textDecoration: 'none', color: 'inherit'}}>Vai alla pagina della
                            stanza singola</Link>
                    </button>
                    */}

                    <div style={{display: 'flex', flexDirection: 'row', flexWrap: 'wrap'}}>
                        {this.state.array.map((room, index) => (
                            <div key={index}
                                 style={{
                                     border: '1px solid #ccc',
                                     borderRadius: '10px',
                                     padding: '5px',
                                     margin: '5px',
                                     width: '100%',
                                 }}>
                                <h3>Nome della Stanza: {room.nome}</h3>
                                <p>ID: {room.id}</p>
                                <p>Descrizione: {room.descrizione}</p>
                                {/* inserisci gli altri dati di cui hai bisogno */}
                                <button>
                                    <Link
                                        to={`/SingleRoom/${room.id}`}
                                        style={{textDecoration: 'none', color: 'inherit'}}
                                    >
                                        Vai alla pagina della stanza
                                    </Link>
                                </button>
                            </div>
                            ))}
                    </div>
                </div>
            </>
        );
    }
}

export default RoomList;
