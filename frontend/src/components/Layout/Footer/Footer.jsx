import Comm from "../../../img/commigo.png";
import './Footer.css'
import '../../../css/MyApp.css'
import React from "react";

export function MyFooter() {
    return (
        <div id={"footer"}>
            <div className={"table-container"}>
                <div className={"table-row"}>
                    <div className={"table-cell"}>
                        <img src={Comm} className='App-logo' alt='nessuna immagine'></img>
                    </div>
                </div>
            </div>
            <div className={"footerLayout"}>
                <p>
                    Carmine Detta
                </p>
                <p>
                    Salvatore Alberti
                </p>
                <p>
                    Vincenzo Cutolo
                </p>
                <p>
                    Francesco Gatto
                </p>
                <p>
                    Michele Pesce
                </p>
                <p>
                    Domenico Cavaliere
                </p>
            </div>
        </div>
    );
}