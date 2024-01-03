import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import {MyHeader} from '../components/Header/Header';
import {MyFooter} from "../components/Footer/Footer";
import Facebook from "../components/FacebookLogin/FacebookLoginButton";
export const Login = () =>{
    return(
        <>
            <header>
                <MyHeader />
            </header>
            <section className={"sec"} id={"sec1"}>
                <div className={"table-container"}>
                    <div className={"table-row"}>
                        <div className={"table-cell"}>
                            <Facebook />
                        </div>
                    </div>
                </div>
            </section>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
}