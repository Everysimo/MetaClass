import React from "react";
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import {useNavigate} from "react-router-dom";


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
                <div className={"table-cell"}>

                </div>
                <div className={"table-cell"}>
                    <div className={"table-row"}>
                        <h2>Pagina della stanza singola</h2>

                        {/*ci va tutta la funzione della pagina*/}

                        <button onClick={handleGoToModifyDataRoom}>Modifica la stanza</button>
                        <button onClick={handleGoToModifyScenario}>Modifica lo scenario della stanza</button>

                    </div>
                    <div className={"table-row"}>

                    </div>
                </div>
            </div>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}