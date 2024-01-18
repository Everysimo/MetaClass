import '../css/unlogged.css';
import React from "react";
import Video from '../media/VRvideo.mp4';
import { Link } from "react-router-dom";
import {MyHeader} from "../components/Layout/Header/Header";
import {MyFooter} from "../components/Layout/Footer/Footer";

export default function UnloggedHome(){
    return(
        <>
            <header >
                <MyHeader/>
            </header>
            <section className="showcase">
                <video muted loop autoPlay playsInline>
                    <source src={Video} type="video/mp4"/>
                </video>
                <div className="overlay"></div>
                <div className="text">
                <h2>metaclass </h2>
                    <h3>a doorway to the future</h3>
                    <p>
                        Nato da un piccolo progetto universitario, il nostro obiettivo è
                        quello di offrire un portale organizzativo per la nostra VR app!
                    </p>
                    <Link to="/login">Entra anche tu!</Link>
                </div>
            </section>
            <footer id={"unloggedFooter"}>
                <MyFooter />
            </footer>
        </>
    );
}