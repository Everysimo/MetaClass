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
            <RoomList/>
        <footer>
            <MyFooter/>
        </footer>
        </>
    );
}