import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import {MyHeader} from '../components/Header/Header';
import {MyFooter} from "../components/Footer/Footer";
import {SectionGenerator} from "../components/functions";
export const Login = () =>{
    return(
        <body>
            <header>
                <MyHeader />
            </header>
            <SectionGenerator class={"sec"} ID={"sec1"}/>
            <footer>
                <MyFooter />
            </footer>
        </body>
    );
}