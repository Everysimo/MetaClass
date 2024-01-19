import {MyHeader} from "../components/Layout/Header/Header";
import {MyFooter} from "../components/Layout/Footer/Footer";
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";

export const MeetingXQuestionario = () => {

    const [arrayMeeting, setArray] = useState([]);


    useEffect(() => {
        fetchMeeting();
    }, []);

    const requestOption = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchMeeting = async () => {
        try {
            const response = await fetch('http://localhost:8080/visualizzaQuestionari', requestOption);

            if (!response.ok) {
                throw new Error('Errore nel recupero dei meeting senza questionari.');
            }

            const data = await response.json();
            //console.log("array dei meeting", data);
            const meetings = data.value || [];
            setArray(meetings);

/*
            arrayMeeting.forEach((parametro, indice) => {
                const nome = parametro.nome;
                console.log(`Nome: ${indice + 1}: ${nome}`);
            });
*/

        } catch (error) {
            console.error('Errore durante il recupero deim:', error.message);
        }
    };

    return(
        <>
            <header>
                <MyHeader/>
            </header>

            <h3>MEETING CON QUESTIONARIO DA COMPILARE</h3>

            <div className={"table-container"} style={{background: "transparent"}}>
                {arrayMeeting.map((meeting) => (
                    <div key={meeting.id} className={"table-row"}>
                        <span className={"table-cell"}><h3>Nome del Meeting: {meeting.nome}</h3></span>
                        <span className={"table-cell"}><p>Inizio: {meeting.inizio}</p></span>
                        <span className={"table-cell"}><p>Fine: {meeting.fine}</p></span>
                        <span className={"table-cell"}><p>ID della Stanza: {meeting.id_stanza.id}</p></span>

                        <button>
                            <Link to={`/Questionario/${meeting.id}`}
                                  style={{color: 'inherit', textDecoration: 'none'}}>
                                Compila Questionario
                            </Link>
                        </button>

                    </div>
                ))}
            </div>


            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}
