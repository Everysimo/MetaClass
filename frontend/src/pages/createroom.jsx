
import React from 'react'
import { MyHeader } from '../components/Header/Header'
import { MyFooter } from '../components/Footer/Footer'

import "../css/createroom.css";
import MyForm from "../components/CreateRoomForm/MyCreateForm";

export const CreateRoom = () => {

    return(
        <>
            <header>
                <MyHeader/>
            </header>
            <section >
                <div className="total-body">
                    <div>
                        <div>
                            <MyForm/>
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
