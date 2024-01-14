import '../css/unlogged.css';
import React from "react";
import Video from '../media/VRvideo.mp4';
import { Link } from "react-router-dom";
import {MyHeader} from "../components/Header/Header";

export default function UnloggedHome(){
    return(
        <>
            <header>
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
                        Nato da un piccolo progetto universitario, il nostro obiettivo &egrave
                        quello di offrire un portale organizzativo per la nostra VR app!
                    </p>
                    <Link to="/login">Entra anche tu!</Link>
                </div>
                <ul className="social">
                    <li><a href="#"><img src="https://i.ibb.co/x7P24fL/facebook.png"/></a></li>
                    <li><a href="#"><img src="https://i.ibb.co/Wnxq2Nq/twitter.png"/></a></li>
                    <li><a href="#"><img src="https://i.ibb.co/ySwtH4B/instagram.png"/></a></li>
                </ul>
            </section>
        </>
    );
}