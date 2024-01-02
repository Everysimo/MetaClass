// LoggedIn.js
import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import '../css/LoggedHome.css';
import { MyHeader } from '../components/Header/Header';
import { MyFooter } from "../components/Footer/Footer";
import {useNavigate} from "react-router-dom";

function useHandleLogout() {
    const navigate = useNavigate();
    const handleLogout = () => {
        localStorage.setItem('isLoggedIn', 'false');
        navigate('/');
    };
    return handleLogout;
}
export const LoggedIn = () => {
    const handleLogout = useHandleLogout();

    return (
        <body>
        <header>
            <MyHeader />
        </header>
        <section className={"sec"} id={"sec1"}>
            <div className={"logoutForm"}>
                <div className={"table-container"}>
                    <div className={"table-row"}>
                        <div className={"table-cell"}>
                            <button onClick={handleLogout}>Logout</button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <footer>
            <MyFooter />
        </footer>
        </body>
    );
};
