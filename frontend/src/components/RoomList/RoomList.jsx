import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {useNavigate} from "react-router-dom";

const RoomList = () => {
    const navigate = useNavigate();
    const [data, setData] = useState([]);



    useEffect(() => {
        // Effettua la chiamata al backend quando il componente viene montato
        axios.get('http://localhost:8080/visualizzaStanze')
            .then(response => {
                // Ricevi i dati dal backend e aggiorna lo stato
                setData(response.data);
            })
            .catch(error => {
                console.error('Errore nella chiamata al backend:', error);
            });
    }, []); // L'array vuoto come dipendenza assicura che la chiamata avvenga solo una volta all'avvio del componente

    const handleGoToSingleRoom= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/SingleRoom`);
    };

    return (
        <div>
            <h1>Elenco Stanze</h1>
            <ul>
                {data.map(item => (
                    <li key={item.id}>
                        {item.nome} - {item.descrizione}
                    </li>
                ))}
            </ul>

            <button onClick={handleGoToSingleRoom}>vai alla pagina della stanza singola</button>
        </div>
    );
};

export default RoomList;
