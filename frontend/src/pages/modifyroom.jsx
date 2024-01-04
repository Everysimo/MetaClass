
import React from 'react'
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import MyModifyForm from "../components/ModifyRoomForm/MyModifyForm";

export const ModifyRoom = () => {

    return(
        <>
            <header>
                <MyHeader/>
            </header>
            <section>
                <div className="total-body">
                    <div>
                        <div>
                            <MyModifyForm/>
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