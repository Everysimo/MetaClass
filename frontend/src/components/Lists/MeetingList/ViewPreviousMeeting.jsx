import {useEffect, useState} from "react";
import {Link} from "react-router-dom";

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
            <div className={"table-container"} style={{background: "transparent"}}>
                {meetingArray.map((meeting) => (
                    <div key={meeting.id} className={"table-row"}>
                        <span className={"table-cell"}><h3>Nome del Meeting: {meeting.nome}</h3></span>
                        <span className={"table-cell"}><p>Inizio: {meeting.inizio}</p></span>
                        <span className={"table-cell"}><p>Fine: {meeting.fine}</p></span>
                        <span className={"table-cell"}><p>ID della Stanza: {meeting.id_stanza.id}</p></span>
                        {/*
                        <button>
                            <Link to={`/Questionario/${meeting.id}`}
                                  style={{color: 'inherit', textDecoration: 'none'}}>
                                Compila Questionario
                            </Link>
                        </button>
                        */}
                    </div>
                ))}
                <p>Vai ai meeting in cui ancora non hai compilato il form:</p>
                <button>
                    <Link to={`/meetingwithoutQuestionario`}
                          style={{color: 'inherit', textDecoration: 'none'}}>
                        Visualizza
                    </Link>
                </button>
            </div>
        </>
    );
}