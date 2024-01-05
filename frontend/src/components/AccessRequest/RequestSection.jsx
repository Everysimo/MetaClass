
import React, {useState} from 'react'

import "./RequestAccess.css"
export const RequestSection = () => {
    //const [accessRequests, setAccessRequests] = useState([]);

    const accessRequests = [
        { userId: 1, userName: 'Gino' },
        { userId: 2, userName: 'MArio' },
        // Aggiungi altri dati di richieste di accesso secondo necessità
    ];
    /*

    useEffect(() => {
        // Funzione per ottenere le richieste di accesso dal backend
        const fetchAccessRequests = async () => {
            try {
                const response = await axios.get('URL_DEL_BACKEND'); // Sostituisci 'URL_DEL_BACKEND' con l'effettivo URL del tuo backend
                setAccessRequests(response.data); // Supponendo che il backend restituisca un array di richieste
            } catch (error) {
                console.error('Errore nel recupero delle richieste di accesso:', error);
            }
        };

        // Chiamata alla funzione per ottenere le richieste di accesso
        fetchAccessRequests();
    }, []); // L'array vuoto come secondo argomento fa sì che useEffect venga eseguito solo al montaggio del componente
*/
    const handleAccept = async (userId) => {
            const requestOption ={
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userId)
            };

            try{
                const response = await fetch('http://localhost:8080', requestOption);
                const responseData = await response.json();

                console.log("Risposta dal server", responseData);
            }catch(error){
                console.error('errore: ', error);
            }

        console.log(`Accettato l'accesso per l'utente con ID ${userId}`);
    };

    const handleReject  = async (userId) => {

        const requestOption ={
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userId)

        };

        try{
            const response = await fetch('http://localhost:8080', requestOption);
            const responseData = await response.json();

            console.log("Risposta dal server", responseData);
        }catch(error){
            console.error('errore: ', error);
        }
        console.log(`Rifiutato l'accesso per l'utente con ID ${userId}`);
    };

    return(
        <>
            <div className="access-management-container">
                <h2>Richieste di accesso</h2>
                {accessRequests.map((request) => (
                    <div key={request.userId} className="card-container">
                        <div className="user-info">
                            <h3>{request.userName}</h3>
                        </div>
                        <div className="button-container">
                            <button className={'button-field'} onClick={() => handleAccept(request.userId)}>Accetta</button>
                            <button className={'button-field'} onClick={() => handleReject(request.userId)}>Rifiuta</button>
                        </div>
                    </div>
                ))}
            </div>
        </>
    )
};
