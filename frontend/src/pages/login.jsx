import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import { MyHeader } from '../components/Layout/Header/Header';
import { MyFooter } from "../components/Layout/Footer/Footer";
import Facebook from "../components/Buttons/FacebookLoginButton/FacebookLoginButton";
import isLoggedIn from "../components/Layout/Header/Menu/loginCheck";
import { useNavigate } from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUser} from "@fortawesome/free-solid-svg-icons";

export const Login = () => {
    const navigate = useNavigate();

    return (
        isLoggedIn() ? (
            navigate("/LoggedInHome")
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