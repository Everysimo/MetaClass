
import React from 'react'
import {MyHeader} from "../components/Layout/Header/Header";
import {MyFooter} from "../components/Layout/Footer/Footer";
import RequestSection from "../components/Forms/AccessRequest/RequestSection";
import "../css/accessManagement.css";
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