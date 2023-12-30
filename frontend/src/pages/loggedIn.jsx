/**/
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import React from "react";

export const loggedIn = () =>{
    return(
        <body>
            <header>
                <MyHeader />
            </header>
            <section className={"sec"}>
            </section>
            <footer>
                <MyFooter />
            </footer>
        </body>
    );
}