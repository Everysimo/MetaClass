import React from "react";
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import {useNavigate, useParams} from "react-router-dom";
import CalendarComp from "../components/Calendar/CalendarComp";


export const SingleRoom = () =>{
    const navigate = useNavigate();
    const { id: id_stanza } = useParams();      //si usa useParams per farsi passare il parametro

    sessionStorage.setItem('idStanza', id_stanza)
    const handleGoToModifyDataRoom= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/modifyroom/ ${id_stanza}`);
    };
    const handleGoToChangeScenario= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/changescenario/ ${id_stanza}`);
    };

    return(
        <>
            <header>
                <MyHeader />
            </header>
            <div className={"table-container"}>
                <div className={"table-row"}>
                    <span className={"table-cell"}><h1>Pagina della stanza singola</h1></span>
                    <h3>ecco la stanza: {id_stanza}</h3>
                </div>
                <div className={"table-row"}>
                    <div className={"table-cell"}>
                        <h2>Schedula un nuovo meeting</h2>
                        <CalendarComp />
                    </div>
                    <div className={"table-cell"}>
                        <div className={"table-row"}>
                            {/*ci va tutta la funzione della pagina*/}

                            <button onClick={handleGoToModifyDataRoom}>Modifica la stanza</button>
                            <button onClick={handleGoToChangeScenario}>Modifica lo scenario della stanza</button>

                        </div>
                        <div className={"table-row"}>

                        </div>
                    </div>
                </div>
            </div>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}