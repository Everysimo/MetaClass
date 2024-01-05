
import React from 'react'
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import {RequestSection} from "../components/AccessRequest/RequestSection";
import "../css/accessManagement.css";
import MyCreateForm from "../components/CreateRoomForm/MyCreateForm";
const GestioneAccessi = () => {

    return(

        <>
            <header>
                <MyHeader/>
            </header>
            <section>
                <div className="total-body">
                    <div>
                       <div>
                           <RequestSection/>
                       </div>
                    </div>
                </div>
            </section>
            <footer>
            <MyFooter/>
            </footer>
        </>
);
}

export default GestioneAccessi;