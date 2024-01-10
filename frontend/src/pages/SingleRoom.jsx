import React from "react";
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import {useNavigate, useParams} from "react-router-dom";
import CalendarComp from "../components/Calendar/CalendarComp";


export const SingleRoom = () =>{
    const navigate = useNavigate();
    const { id: id_stanza } = useParams();      //si usa useParams per farsi passare il parametro

    sessionStorage.setItem('idStanza', id_stanza);


    const handleGoToUserList= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/userListRoom/ ${id_stanza}`);
    };

    const handleGoToModifyDataRoom= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/modifyroom/ ${id_stanza}`);
    };
    const handleGoToChangeScenario= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/changescenario/ ${id_stanza}`);
    };
    const handleGoToAccessManagement= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/accessManagement/ ${id_stanza}`);
    };

    const handleGoToBannedUserList= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/bannedUserList/ ${id_stanza}`);
    };

    return(
        <>
            <header>
                <MyHeader />
            </header>
            <section className={"contentSec"}>
                <h1>Stanza {id_stanza}</h1>
                <div className={"masterDiv"}>
                    <div className={"childDiv"}>
                        <h2>Schedula un nuovo meeting</h2>
                        <CalendarComp/>
                    </div>
                    <div className={"childDiv"}>
                        <button onClick={handleGoToUserList}>Visualizza Lista Utenti in Stanza</button>
                        <button onClick={handleGoToModifyDataRoom}>Modifica la stanza</button>
                        <button onClick={handleGoToChangeScenario}>Modifica lo scenario della stanza</button>
                        <button onClick={handleGoToAccessManagement}>Gestione degli accessi</button>
                        <button onClick={handleGoToBannedUserList}>Visualizza Lista Utenti Bannati</button>
                    </div>
                </div>
            </section>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}