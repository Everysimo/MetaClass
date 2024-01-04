import React, {useEffect, useState} from 'react';
import axios from "axios";

const UserData = () => {
    const Id = localStorage.getItem("UserMetaID");

    const [data, setData] = useState([]);
        const [error, setError] = useState(null);

        useEffect(() => {
            const fetchData = async () => {
                try {
                    // Sostituisci con l'URL effettivo del tuo backend
                    const response = await axios.get('http://localhost:8080/visualizzaStanze');

                    // Aggiorna lo stato con i dati ricevuti dal backend
                    setData(response.data);
                } catch (error) {
                    // Gestisci gli errori qui
                    setError(error.message);
                }
            };

            fetchData();
        }, [Id]); // L'array vuoto assicura che la richiesta venga effettuata solo al montaggio del componente

        return (
            <div>
                {error && <p>Errore durante il recupero dei dati: {error}</p>}

                {data.length > 0 && (
                    <div>
                        <h2>Dati dal backend:</h2>
                        <ul>
                            {data.map((item, index) => (
                                <li key={index}>
                                    {/* Visualizza le proprietà degli oggetti come preferisci */}
                                    <strong>ID:</strong> {item.id}, <strong>Nome:</strong> {item.nome}, <strong>Cognome:</strong> {item.cognome}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        );
};

export default UserData;

