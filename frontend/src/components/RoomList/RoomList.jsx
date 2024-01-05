import React, { useState, useEffect } from 'react';
import axios from 'axios';

const DataDisplayComponent = () => {
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

    return (
        <div>
            <h1>Dati dal Backend</h1>
            <ul>
                {data.map(item => (
                    <li key={item.id}>
                        {item.nome} - {item.descrizione}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default DataDisplayComponent;
