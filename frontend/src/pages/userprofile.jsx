
import React from "react"
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import UserData from "../components/Profile/UserData";

export const UserProfile = () => {

    return (

    <body>
    <header>
        <MyHeader/>
    </header>

    <section>
        <div>
            <UserData/>
        </div>
    </section>

    <footer>
        <MyFooter/>
    </footer>
    </body>
    );
}