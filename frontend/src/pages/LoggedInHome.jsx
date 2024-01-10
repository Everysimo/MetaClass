// LoggedIn.js
import React, { useState } from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import '../css/LoggedHome.css';
import { MyHeader } from '../components/Header/Header';
import { MyFooter } from "../components/Footer/Footer";
import LogoutButton from "../components/LogoutButton/logoutButton";
import { useNavigate } from 'react-router-dom';
import AvviaMeeting from "../components/GestioneMeeting/AvviaMeeting";
import CreaCategoria from "../components/CreaCategoria/CreaCategoria";
import CreaScenario from "../components/Forms/CreaScenarioForm/CreaScenario";
import ModificaScenario from "../components/Forms/CreaScenarioForm/ModificaScenario";
import AccediStanza from "../components/enterRoomPopup/accediStanza";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChalkboardUser, faCirclePlus, faDoorOpen, faUser} from "@fortawesome/free-solid-svg-icons";

export const LoggedInHome = () => {
    const nome = sessionStorage.getItem('nome');
    const isAdmin = sessionStorage.getItem('isAdmin') === 'true';
    const [isAvviaMeetingVisible, setIsAvviaMeetingVisible] = useState(false);
    const [isVisibleScen, setIsVisibleScen] = useState(false);
    const [isVisibleModScen, setIsVisibleModScen] = useState(false);
    const [isVisibleCat, setIsVisibleCat] = useState(false);
    const [isVisibleAcc, setIsVisibleAcc] = useState(false);

    //fatte per prova per vedere se mi porta alla pagina tramite button
    const navigate = useNavigate();

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
        setIsAvviaMeetingVisible(false);
        setIsVisibleScen(false);
        setIsVisibleModScen(false);
        setIsVisibleCat(false);
        setIsVisibleAcc(false);
    };

    return (
        <>
            <header>
                <MyHeader />
            </header>
            <section className={"contentSec"}>
                <h1>BENTORNATO, {nome}</h1>
            </section>
            <section className={"layout"}>
                <div>
                    <FontAwesomeIcon icon={faUser} size="2xl" style={{color: "#c70049",}} />
                    <h2>Account</h2>
                    <button onClick={handleGoToProfile}>vai ad Account</button>
                </div>
                <div>
                    <FontAwesomeIcon icon={faChalkboardUser} size="2xl" style={{color: "#c70049",}} />
                    <h2>Le mie Stanze</h2>
                    <button onClick={handleGoToRoomList}>vai all'elenco delle stanze</button>
                </div>
                <div>
                    <FontAwesomeIcon icon={faCirclePlus} size="2xl" style={{color: "#c70049",}} />
                    <h2>Creazione stanza</h2>
                    <button onClick={handleGoToCreateRoom}>Crea stanza</button>
                </div>
                <div>
                    <FontAwesomeIcon icon={faDoorOpen} size="2xl" style={{color: "#c70049",}} />
                    <h2>Ingresso stanza</h2>
                    <button onClick={() => {
                        closeAllComponents();
                        setIsVisibleAcc(prevVisibility => !prevVisibility)
                    }}
                    >
                        Accedi Stanza
                    </button>
                </div>
                {isVisibleAcc &&
                    <div>
                        <AccediStanza onClose={() => setIsVisibleAcc(false)}/>
                    </div>
                }
            </section>
            <section className={"contentSec"}>
                    <div className={"childDiv"}>
                        <h5> IN QUANTO ORGANIZZATORE...</h5>
                        <button onClick={() => {
                            closeAllComponents();
                            setIsAvviaMeetingVisible(prevVisibility => !prevVisibility)
                        }}
                        >
                            AvviaMeeting
                        </button>
                        {isAvviaMeetingVisible &&
                            <AvviaMeeting id_meeting={1} onClose={() => setIsAvviaMeetingVisible(false)}/>
                        }
                    </div>
                    {isAdmin &&
                        <div className={"childDiv"}>
                            <h5> IN QUANTO ADMIN...</h5>

                            <button onClick={() => {
                                closeAllComponents();
                                setIsVisibleCat(prevVisibility => !prevVisibility)
                            }}>Crea Categoria
                            </button>
                            {isVisibleCat && <CreaCategoria onClose={() => setIsVisibleCat(false)}/>}

                            <button onClick={() => {
                                closeAllComponents();
                                setIsVisibleScen(prevVisibility => !prevVisibility)
                            }}>Crea Scenario
                            </button>
                            {isVisibleScen && <CreaScenario onClose={() => setIsVisibleScen(false)}/>}

                            <button onClick={() => {
                                closeAllComponents();
                                setIsVisibleModScen(prevVisibility => !prevVisibility)
                            }}>Modifica Scenario
                            </button>
                            {isVisibleModScen && <ModificaScenario onClose={() => setIsVisibleModScen(false)}/>}

                            <LogoutButton/>
                        </div>
                    }
            </section>
            <footer>
            <MyFooter/>
            </footer>

        </>
    );
};
