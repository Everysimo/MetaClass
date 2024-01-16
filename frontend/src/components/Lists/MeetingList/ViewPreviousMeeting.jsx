import {useEffect, useState} from "react";

export const ViewPreviousMeeting = () => {
    const [meetingArray, setMeetingArray] = useState([]);

    useEffect(() => {
        fetchPreviousMeeting();
    }, []);

    const requestOption = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchPreviousMeeting = async () => {
        try {
            const response = await fetch(`http://localhost:8080/visualizzaMeetingPrecedenti`, requestOption);

            if (!response.ok) {
                throw new Error('Errore nella richiesta');
            }

            const data = await response.json();
            console.log('data:', data);

            // Estrai l'array di meeting da data.value
            const meetings = data.value || [];
            setMeetingArray(meetings);

            meetingArray.forEach((parametro, indice) => {
                const nome = parametro.nome;
                console.log(`Descrizione ${indice + 1}: ${nome}`);
            });

        } catch (error) {
            console.error('Errore durante il recupero della lista di utenti:', error);
        }
    }

    return(

        <>
            <h3>TUTTI I MIEI MEETING</h3>
            <div>
                {meetingArray.map((meeting) => (
                    <div key={meeting.id}>
                        <h3>Nome del Meeting: {meeting.nome}</h3>
                        <p>Inizio: {meeting.inizio}</p>
                        <p>Fine: {meeting.fine}</p>
                        <p>ID della Stanza: {meeting.id_stanza.id}</p>
                        {/* Aggiungi altri campi se necessario */}
                        <hr />
                    </div>
                ))}
            </div>
        </>
    );
}