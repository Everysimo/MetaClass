// LoggedIn.js
import React, {useEffect, useState} from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import '../css/LoggedHome.css';
import { MyHeader } from '../components/Layout/Header/Header';
import { MyFooter } from "../components/Layout/Footer/Footer";
import { useNavigate } from 'react-router-dom';
import CreaCategoria from "../components/Forms/CreaCategoria/CreaCategoria";
import CreaScenario from "../components/Forms/CreaScenarioForm/CreaScenario";
import AccediStanza from "../components/Forms/enterRoomForm/accediStanza";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCalendarDays, faImage, faList, faUser} from "@fortawesome/free-solid-svg-icons";
import RoomList from "../components/Lists/RoomList/RoomList";
import MyCreateForm from "../components/Forms/CreateRoomForm/MyCreateForm";

export const LoggedInHome = () => {
    const nome = sessionStorage.getItem('nome');
    const isAdmin = sessionStorage.getItem('isAdmin') === 'true';
    const [isVisibleScen, setIsVisibleScen] = useState(false);
    const [isVisibleModScen, setIsVisibleModScen] = useState(false);
    const [isVisibleCat, setIsVisibleCat] = useState(false);

    //fatte per prova per vedere se mi porta alla pagina tramite button
    const navigate = useNavigate();
    useEffect(() => {
        const resizeSideNav = () => {
            const mainSection = document.querySelector('.roomSec');
            const sideNav = document.querySelector('.side-nav');

            if (mainSection && sideNav) {
                const mainHeight = window.getComputedStyle(mainSection).height;
                sideNav.style.maxHeight = mainHeight;
            }
        };

        window.addEventListener('resize', resizeSideNav);
        resizeSideNav();

        return () => {
            window.removeEventListener('resize', resizeSideNav);
        };
    }, []);

    //funzione per andare alla pagina tramite onClick
    const handleGoToProfile= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/Account`);
    };
    const handleGoToMeetings= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/previousMeeting`);
    };

    const closeAllComponents = () => {
        setIsVisibleScen(false);
        setIsVisibleModScen(false);
        setIsVisibleCat(false);
    };

    return (
        <>
            <header>
                <MyHeader />
            </header>
            <main className={"bg"}>
                <section className={"roomSec"}>
                    <h1>BENTORNATO, {nome}</h1>
                    <div className={"layout"}>
                        <div
                            className={"transWhiteBg"}
                            onClick={handleGoToProfile}
                        >
                            <FontAwesomeIcon icon={faUser} size="2xl" style={{color: "#c70049",}}/>
                            <h2>Account</h2>
                            <p
                                style={{fontSize: "14px", textAlign: "center",}}
                            >
                                Visualizza e modifica i dettagli del tuo account
                            </p>
                        </div>
                        <MyCreateForm/>
                        <AccediStanza/>
                        <div
                            className={"transWhiteBg"}
                            onClick={handleGoToMeetings}
                        >
                            <FontAwesomeIcon icon={faCalendarDays} size="2xl" style={{color: "#c70049",}} />
                            <h2>Bacheca meetings</h2>
                            <p
                                style={{fontSize: "14px", textAlign: "center",}}
                            >
                                Visualizza tutti i meetings a cui hai partecipato
                            </p>
                        </div>
                        {isAdmin &&
                            <>
                                <div
                                    className={"transWhiteBg"}
                                    onClick={() => {
                                        closeAllComponents();
                                        setIsVisibleCat(prevVisibility => !prevVisibility)
                                    }}>
                                    <FontAwesomeIcon icon={faList} size="2xl" style={{color: "#c70049",}} />
                                    <h2>Crea Categoria</h2>
                                    <p
                                        style={{fontSize: "14px", textAlign: "center",}}
                                    >Funzione amministratore per creare una nuova categoria</p>
                                </div>
                                {isVisibleCat &&
                                    <CreaCategoria onClose={() => setIsVisibleCat(false)}/>}
                                <div
                                    className={"transWhiteBg"}
                                    onClick={() => {
                                        closeAllComponents();
                                        setIsVisibleScen(prevVisibility => !prevVisibility)
                                    }}>
                                    <FontAwesomeIcon icon={faImage} size="2xl" style={{color: "#c70049",}} />
                                    <h2>Crea Scenario</h2>
                                    <p
                                        style={{fontSize: "14px", textAlign: "center",}}
                                    >Funzione amministratore per creare un nuovo scenario</p>
                                </div>
                                {isVisibleScen && <CreaScenario onClose={() => setIsVisibleScen(false)}/>}
                            </>
                        }
                    </div>
                </section>
                <aside className={"side-nav"}>
                    <h2>Le mie stanze:</h2>
                    <RoomList/>
                </aside>
            </main>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
};
