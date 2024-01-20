import {MyHeader} from "../components/Layout/Header/Header";
import {MyFooter} from "../components/Layout/Footer/Footer";
import React from "react";
import SelectScenario from "../components/Forms/SelectNewScenario/SelectScenario";

export const SelezionaScenario = () => {

    return(
        <>
            <header>
                <MyHeader/>
            </header>
            <section>
                <div className="total-body">
                    <div>
                        <div>
                            <SelectScenario/>
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