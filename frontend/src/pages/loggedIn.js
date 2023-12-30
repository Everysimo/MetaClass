/**/
import {MyFooter, MyHeader} from "../components/MyApp-components";
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