// LoggedIn.js
import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import '../css/LoggedHome.css';
import { MyHeader } from '../components/Header/Header';
import { MyFooter } from "../components/Footer/Footer";
import LogoutButton from "../components/LogoutButton/logoutButton";

export const LoggedIn = () => {
    const nome = localStorage.getItem('nome');
    return (
        <>
        <header>
            <MyHeader />
        </header>
        <section className={"sec"} id={"sec1"}>
            <div className={"logoutForm"}>
                <div className={"table-container"}>
                    <div className={"table-row"}>
                        <div className={"table-cell"}>
                            <h2>Welcome back, {nome}</h2>
                            <LogoutButton />
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <footer>
            <MyFooter />
        </footer>
        </>
    );
};
