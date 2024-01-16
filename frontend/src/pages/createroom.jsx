
import React from 'react'
import { MyHeader } from '../components/Layout/Header/Header'
import { MyFooter } from '../components/Layout/Footer/Footer'

import "../css/createroom.css";
import MyCreateForm from "../components/Forms/CreateRoomForm/MyCreateForm";

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
                            <MyCreateForm/>
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
