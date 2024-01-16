import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import { MyHeader } from '../components/Header/Header';
import { MyFooter } from "../components/Footer/Footer";
import Facebook from "../components/FacebookLogin/FacebookLoginButton";
import isLoggedIn from "../components/Header/BurgerButton/loginCheck";
import { useNavigate } from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUser} from "@fortawesome/free-solid-svg-icons";

export const Login = () => {
    const navigate = useNavigate();

    return (
        isLoggedIn() ? (
            navigate("/LoggedInHome"), null
        ) : (
            <>
                <header>
                    <MyHeader />
                </header>
                <main className={"bg"} id={"loginMain"}>
                    <section>
                        <div>
                            <FontAwesomeIcon icon={faUser} style={{color: "#ffffff", fontSize: "48px"}} />
                            <Facebook />
                        </div>
                    </section>
                </main>
                <footer>
                    <MyFooter />
                </footer>
            </>
        )
    );
};
