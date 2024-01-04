import React from "react";
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";

export const SingleRoom = () =>{
    return(
        <>
            <header>
                <MyHeader />
            </header>
            <div className={"table-container"}>
                <div className={"table-cell"}>

                </div>
                <div className={"table-cell"}>
                    <div className={"table-row"}>
                        <!-- functions go there -->
                    </div>
                    <div className={"table-row"}>

                    </div>
                </div>
            </div>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}