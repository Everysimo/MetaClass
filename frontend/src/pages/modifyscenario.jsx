
import React from 'react'
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import SelectScenario from "../components/SelectNewScenario/SelectScenario";

export const SelezionaScenario = () => {

    return(

        <>
            <header>
                <MyHeader/>
            </header>
                    <div className={'total-body'}>
                        <div>
                            <SelectScenario/>
                        </div>
                    </div>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );

}