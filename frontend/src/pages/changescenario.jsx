import {MyHeader} from "../components/Header/Header";
import MyModifyForm from "../components/Forms/ModifyRoomForm/MyModifyForm";
import {MyFooter} from "../components/Footer/Footer";
import React from "react";
import SelectScenario from "../components/SelectNewScenario/SelectScenario";

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