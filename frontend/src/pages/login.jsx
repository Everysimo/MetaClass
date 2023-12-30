import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import {MyHeader} from '../components/Header/Header';
import {MyFooter} from "../components/Footer/Footer";
export const Login = () =>{
    return(
        <body>
            <header>
                <MyHeader />
            </header>

            <footer>
                <MyFooter />
            </footer>
        </body>
    );
}