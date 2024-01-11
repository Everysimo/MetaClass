// LoggedIn.js
import React, {useEffect, useState} from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import '../css/LoggedHome.css';
import { MyHeader } from '../components/Header/Header';
import { MyFooter } from "../components/Footer/Footer";
import LogoutButton from "../components/LogoutButton/logoutButton";
import { useNavigate } from 'react-router-dom';
import CreaCategoria from "../components/CreaCategoria/CreaCategoria";
import CreaScenario from "../components/Forms/CreaScenarioForm/CreaScenario";
import ModificaScenario from "../components/Forms/CreaScenarioForm/ModificaScenario";
import AccediStanza from "../components/Forms/enterRoomForm/accediStanza";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChalkboardUser, faCirclePlus, faDoorOpen, faUser} from "@fortawesome/free-solid-svg-icons";
import RoomList from "../components/RoomList/RoomList";

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

    const handleGoToRoomList= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/visualRoomList`);
    };
    const handleGoToCreateRoom= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/createroom`);
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
            <main>
                <section className={"roomSec"}>
                    <h1>BENTORNATO, {nome}</h1>
                    <div className={"layout"}>
                        <div className={"whiteBg"}>
                            <FontAwesomeIcon icon={faUser} size="2xl" style={{color: "#c70049",}}/>
                            <h2>Account</h2>
                            <button onClick={handleGoToProfile}>vai ad Account</button>
                        </div>
                        <div className={"whiteBg"}>
                            <FontAwesomeIcon icon={faCirclePlus} size="2xl" style={{color: "#c70049",}}/>
                            <h2>Creazione stanza</h2>
                            <button onClick={handleGoToCreateRoom}>Crea stanza</button>
                        </div>
                        <div className={"whiteBg"}>
                            <FontAwesomeIcon icon={faDoorOpen} size="2xl" style={{color: "#c70049",}}/>
                            <h2>Ingresso stanza</h2>
                            <AccediStanza/>
                        </div>
                    </div>
                    {isAdmin &&
                        <section className={"contentSec"}>
                            <div className={"childDiv"}>
                                <h5> IN QUANTO ADMIN...</h5>
                                <button onClick={() => {
                                    closeAllComponents();
                                    setIsVisibleCat(prevVisibility => !prevVisibility)
                                }}>
                                    Crea Categoria
                                </button>
                                {isVisibleCat && <CreaCategoria onClose={() => setIsVisibleCat(false)}/>}

                                <button onClick={() => {
                                    closeAllComponents();
                                    setIsVisibleScen(prevVisibility => !prevVisibility)
                                }}>
                                    Crea Scenario
                                </button>
                                {isVisibleScen && <CreaScenario onClose={() => setIsVisibleScen(false)}/>}

                                <button onClick={() => {
                                    closeAllComponents();
                                    setIsVisibleModScen(prevVisibility => !prevVisibility)
                                }}>
                                    Modifica Scenario
                                </button>
                                {isVisibleModScen && <ModificaScenario onClose={() => setIsVisibleModScen(false)}/>}

                                <LogoutButton/>
                            </div>
                        </section>
                    }
                </section>
                <aside className={"side-nav"}>
                    <h2>Le mie stanze:</h2>
                    <RoomList />
                </aside>
            </main>
            <footer>
                <MyFooter/>
            </footer>

        </>
    );
};
