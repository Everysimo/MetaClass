import React from "react";
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import {useNavigate} from "react-router-dom";
import CalendarComp from "../components/Calendar/CalendarComp";


export const SingleRoom = () =>{
    const navigate = useNavigate();
    const handleGoToModifyDataRoom= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/modifyroom`);
    };
    const handleGoToModifyScenario= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/modifyscenario`);
    };

    return(
        <>
            <header>
                <MyHeader />
            </header>
            <div className={"table-container"}>
                <div className={"table-row"}>
                    <span className={"table-cell"}><h1>Pagina della stanza singola</h1></span>
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
                            <button onClick={handleGoToModifyScenario}>Modifica lo scenario della stanza</button>

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