
import React from "react"
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import RoomList from "../components/RoomList/RoomList";
import {Account} from "./Account";

export const UserProfile = () => {

    return (

    <>
        <header>
            <MyHeader/>
        </header>
        <section className={"sec"}>
            <div>
                <Account />
            </div>
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