import React from "react";
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import RoomList from "../components/RoomList/RoomList";

export const VisualRoomList = () => {

    return (

        <>
        <header>
            <MyHeader/>
        </header>

        <section>
            <div>
                <RoomList/>
            </div>
        </section>

        <footer>
            <MyFooter/>
        </footer>
        </>
    );
}