import React from "react";
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import {useNavigate, useParams} from "react-router-dom";


export const SingleRoom = () =>{
    const navigate = useNavigate();

    const { id: id_stanza } = useParams();      //si usa useParams per farsi passare il parametro

    const handleGoToModifyDataRoom= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/modifyroom/ ${id_stanza}`);
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
                    <h2>Pagina della stanza singola</h2>

                    <h3>ecco la stanza: {id_stanza}</h3>
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
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}